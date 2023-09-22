package com.example.school.service;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.SchoolDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.*;
import com.example.school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final TeacherRepository teacherRepository;
    private final School2TeacherRepository school2TeacherRepository;
    private final ClassOfStudentsRepository classOfStudentsRepository;
    private final StudentRepository studentRepository;


    public void addSchool(SchoolDto schoolDto) {
        School school = setSchool(schoolDto);
        schoolRepository.save(school);
    }

    public List<SchoolDto> getSchools() {
        List<School> schools = schoolRepository.findAll();
        List<SchoolDto> schoolDtoList = new ArrayList<>();
        for (School school : schools) {
            schoolDtoList.add(setSchoolDto(school));
        }
        return schoolDtoList;
    }

    public void deleteSchool(int schoolId) {
        schoolRepository.deleteById(schoolId);
    }

    public SchoolDto getSchool(int schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);
        return setSchoolDto(school.get());
    }

    public void editSchool(SchoolDto schoolDto) {
        Optional<School> school = schoolRepository.findById(schoolDto.getSchoolId());
        school.get().setSchoolName(schoolDto.getSchoolName());
        school.get().setSchoolAddress(schoolDto.getSchoolAddress());

        schoolRepository.save(school.get());
    }

    public List<TeacherDto> getTeachersInSchool(int schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);
        List<School2Teacher> school2TeacherList = school.get().getSchool2TeacherList();
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        for (School2Teacher school2Teacher : school2TeacherList) {
            TeacherDto teacherDto = new TeacherDto();
            teacherDto.setTeacherId(school2Teacher.getTeacher().getTeacherId());
            teacherDto.setTeacherFirstName(school2Teacher.getTeacher().getTeacherFirstName());
            teacherDto.setTeacherLastName(school2Teacher.getTeacher().getTeacherLastName());
            teacherDtoList.add(teacherDto);
        }
        return teacherDtoList;
    }

    public void deleteTeacherFromSeeTeachers(int teacherId, int schoolId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Optional<School> school = schoolRepository.findById(schoolId);
        List<School2Teacher> school2TeacherList = school2TeacherRepository.findAll();
        for (School2Teacher school2Teacher : school2TeacherList) {
            if (school2Teacher.getSchool().equals(school.get()) &&
                    school2Teacher.getTeacher().equals(teacher.get())) {
                school2TeacherRepository.delete(school2Teacher);
            }
        }
    }

    public List<Teacher> getTeachersForSchool(int schoolId) {
        List<Teacher> newList = new ArrayList<>();
        Optional<School> school = schoolRepository.findById(schoolId);
        List<School2Teacher> school2TeacherList = school.get().getSchool2TeacherList();
        List<Teacher> teacherSchoolList = new ArrayList<>();
        for (School2Teacher school2Teacher : school2TeacherList) {
            teacherSchoolList.add(school2Teacher.getTeacher());
        }
        List<Teacher> teachers = teacherRepository.findAll();
        for (Teacher teacher : teachers) {
            if (!teacherSchoolList.contains(teacher)) {
                newList.add(teacher);
            }
        }
        return newList;
    }

    public void addTeacherToSchool(int schoolId, int teacherId) {
        Optional<School> school = schoolRepository.findById(schoolId);
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);

        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setTeacher(teacher.get());
        school2Teacher.setSchool(school.get());
        school2TeacherRepository.save(school2Teacher);
    }

    public List<ClassOfStudentsDto> getClassListForSchool(int schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> {
                    return new RuntimeException("Cannot find school with id " + schoolId);
                });
        List<ClassOfStudentsDto> classOfStudentsDtoList = new ArrayList<>();
        List<ClassOfStudents> classOfStudentsList = school.getClassOfStudentsList();
        for (ClassOfStudents classOfStudents : classOfStudentsList) {
            classOfStudentsDtoList.add(setClassDto(classOfStudents));
        }

        classOfStudentsDtoList.sort(new Comparator<ClassOfStudentsDto>() {
            @Override
            public int compare(ClassOfStudentsDto c1, ClassOfStudentsDto c2) {
                return c1.getClassName().compareTo(c2.getClassName());
            }
        });
        return classOfStudentsDtoList;
    }

    public void addClassOfStudents(ClassOfStudentsDto classOfStudentsDto) {
        classOfStudentsRepository.save(setClassOfStudents(classOfStudentsDto));
    }

    public List<TeacherDto> getTeachersOptions() {
        List<Teacher> teacherList = teacherRepository.findAll();
        List<TeacherDto> options = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            options.add(setTeacherDto(teacher));
        }
        return options;
    }

    public int getSchoolId(int classId) {
        Optional<ClassOfStudents> classOfStudents = classOfStudentsRepository.findById(classId);
        return classOfStudents.get().getSchool().getSchoolId();
    }

    public void deleteClassForSchool(int classId) {
        classOfStudentsRepository.deleteById(classId);
    }

    public List<StudentDto> getStudentsInClass(int classId) {
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> {
                    return new RuntimeException("Cannot find school by id " + classId);
                });
        List<StudentDto> result = new ArrayList<>();
        List<Student> studentList = classOfStudents.getStudents();
        for (Student student : studentList) {
            result.add(setStudentDto(student));
        }
        return result;
    }

    public String getClassName(int classId) {
        Optional<ClassOfStudents> classOfStudents = classOfStudentsRepository.findById(classId);
        return classOfStudents.get().getClassName();
    }

    public void deleteStudentForClass(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    return new RuntimeException("Cannot find school by id " + studentId);
                });
        student.setClassOfStudents(null);
        studentRepository.save(student);
    }

    public List<StudentDto> getListOfStudentsToAddToClass(int classId) {
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> {
                    return new RuntimeException("Cannot find school by id " + classId);
                });
        List<Student> studentList = studentRepository.findAll();
        List<StudentDto> result = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getClassOfStudents() == null) {
                result.add(setStudentDto(student));
            }
        }
        result.sort(new Comparator<StudentDto>() {
            @Override
            public int compare(StudentDto s1, StudentDto s2) {
                return s1.getStudentLastName().compareTo(s2.getStudentLastName());
            }
        });
        return result;
    }

    public void addStudentToClass(int classId, int studentId) {
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> {
                    return new RuntimeException("Cannot find class by id " + classId);
                });
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    return new RuntimeException("Cannot find student by id " + studentId);
                });
        student.setClassOfStudents(classOfStudents);
        studentRepository.save(student);
    }


    private StudentDto setStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(student.getStudentId());
        studentDto.setStudentFirstName(student.getStudentFirstName());
        studentDto.setStudentLastName(student.getStudentLastName());
        studentDto.setStudentDateOfBirth(String.valueOf(student.getStudentDateOfBirth()));
        return studentDto;
    }

    private TeacherDto setTeacherDto(Teacher teacher) {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setTeacherId(teacher.getTeacherId());
        teacherDto.setSchool2TeacherList(teacher.getSchool2TeacherList());
        teacherDto.setTeacherFirstName(teacher.getTeacherFirstName());
        teacherDto.setTeacherLastName(teacher.getTeacherLastName());
        teacherDto.setSubject2TeacherList(teacher.getSubject2TeacherList());
        return teacherDto;
    }

    private ClassOfStudents setClassOfStudents(ClassOfStudentsDto classOfStudentsDto) {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        School school = schoolRepository.findById(classOfStudentsDto.getSchoolId())
                .orElseThrow(() -> {
                    return new RuntimeException("Cannot find class with id " + classOfStudentsDto.getSchoolId());
                });
        classOfStudents.setSchool(school);
        classOfStudents.setClassId(classOfStudentsDto.getClassId());
        classOfStudents.setClassName(classOfStudentsDto.getClassName());

        Teacher teacher = teacherRepository.findById(classOfStudentsDto.getClassMaster())
                .orElseThrow(() -> getRuntimeException(classOfStudentsDto));
        classOfStudents.setClassMaster(teacher);
        return classOfStudents;
    }

    private RuntimeException getRuntimeException(ClassOfStudentsDto classOfStudentsDto) {
        return new RuntimeException("Can not find teacher with id" +
                classOfStudentsDto.getClassMaster());
    }

    private ClassOfStudentsDto setClassDto(ClassOfStudents classOfStudents) {
        ClassOfStudentsDto classOfStudentsDto = new ClassOfStudentsDto();
        classOfStudentsDto.setClassId(classOfStudents.getClassId());
        classOfStudentsDto.setClassName(classOfStudents.getClassName());
        classOfStudentsDto.setClassMasterName(classOfStudents.getClassMaster().getTeacherFirstName() +
                " " + classOfStudents.getClassMaster().getTeacherLastName());
        return classOfStudentsDto;
    }

    private SchoolDto setSchoolDto(School school) {
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolId(school.getSchoolId());
        schoolDto.setSchoolName(school.getSchoolName());
        schoolDto.setSchoolAddress(school.getSchoolAddress());
        return schoolDto;
    }

    private School setSchool(SchoolDto schoolDto) {
        School school = new School();
        school.setSchoolId(schoolDto.getSchoolId());
        school.setSchoolName(schoolDto.getSchoolName());
        school.setSchoolAddress(schoolDto.getSchoolAddress());
        return school;
    }

}
