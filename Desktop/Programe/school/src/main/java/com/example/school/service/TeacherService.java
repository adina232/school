package com.example.school.service;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.*;
import com.example.school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final School2TeacherRepository school2TeacherRepository;
    private final ClassOfStudentsRepository classOfStudentsRepository;
    private final Teacher2ClassRepository teacher2ClassRepository;


    public void addTeacher(TeacherDto teacherDto) {
        Teacher teacher = setTeacher(teacherDto);
        teacherRepository.save(teacher);
    }

    public List<TeacherDto> getTeachers() {
        List<Teacher> teacherList = teacherRepository.findAll();
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            teacherDtoList.add(setTeacherDto(teacher));
        }
        return teacherDtoList;
    }

    public void deleteTeacher(int teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    public TeacherDto getTeacherDto(int teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return setTeacherDto(teacher.get());
    }

    public void editTeacher(TeacherDto teacherDto) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherDto.getTeacherId());
        teacher.get().setTeacherFirstName(teacherDto.getTeacherFirstName());
        teacher.get().setTeacherLastName(teacherDto.getTeacherLastName());

        teacherRepository.save(teacher.get());
    }

    public List<School> getTeacherSchools(int teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        List<School2Teacher> school2TeacherList = teacher.get().getSchool2TeacherList();
        List<School> schools = schoolRepository.findAll();
        List<School> teacherSchools = new ArrayList<>();
        for (School2Teacher school2Teacher : school2TeacherList) {
            for (School school : schools) {
                if (school2Teacher.getSchool().equals(school)) {
                    teacherSchools.add(school);
                }
            }
        }
        return teacherSchools;
    }

    public List<School> getSchoolsForTeacherToAdd(int teacherId) {
        List<School> newList = new ArrayList<>();
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        List<School2Teacher> school2TeacherList = teacher.get().getSchool2TeacherList();
        List<School> schoolTeacherList = new ArrayList<>();
        for (School2Teacher school2Teacher : school2TeacherList) {
            schoolTeacherList.add(school2Teacher.getSchool());
        }
        List<School> schools = schoolRepository.findAll();
        for (School school : schools) {
            if (!schoolTeacherList.contains(school)) {
                newList.add(school);
            }
        }
        return newList;
    }

    public void addSchoolToTeacher(int teacherId, int schoolId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Optional<School> school = schoolRepository.findById(schoolId);

        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setTeacher(teacher.get());
        school2Teacher.setSchool(school.get());
        school2TeacherRepository.save(school2Teacher);
    }

    public void deleteSchoolFromTeacher(int teacherId, int schoolId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Optional<School> school = schoolRepository.findById(schoolId);
        List<School2Teacher> school2TeacherList = school2TeacherRepository.findAll();
        for (School2Teacher school2Teacher : school2TeacherList) {

            if (school2Teacher.getSchool().equals(school.get()) &&
                    school2Teacher.getTeacher().equals(teacher.get())) {
                school2TeacherRepository.delete(school2Teacher);
                teacherRepository.save(teacher.get());
            }
        }
    }

    public String getTeacherLastName(int teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return teacher.get().getTeacherLastName();
    }

    public String getTeacherFirstName(int teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        return teacher.get().getTeacherFirstName();
    }

    public List<ClassOfStudentsDto> getListOfClassesForTeacher(int teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> getRuntimeException(teacherId));
        List<Teacher2Class> teacher2ClassList = teacher.getTeacher2ClassList();
        List<ClassOfStudents> classOfStudentsList = new ArrayList<>();
        for (Teacher2Class teacher2Class : teacher2ClassList) {
            classOfStudentsList.add(teacher2Class.getClassOfStudents());
        }
        List<ClassOfStudentsDto> classOfStudentsDtoList = new ArrayList<>();
        for (ClassOfStudents classOfStudents : classOfStudentsList) {
            classOfStudentsDtoList.add(setClassOfStudentDto(classOfStudents));
        }
        return classOfStudentsDtoList;
    }

    public List<ClassOfStudents> getClassListForTeacherToAdd(int schoolId, int teacherId) {
        List<ClassOfStudents> result = new ArrayList<>();
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> getRuntimeException(schoolId));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> getRuntimeException(teacherId));
        List<Teacher2Class> teacher2ClassList = teacher.getTeacher2ClassList();
        List<ClassOfStudents> classOfStudentsListTeacher = new ArrayList<>();
        for (Teacher2Class teacher2Class : teacher2ClassList) {
            classOfStudentsListTeacher.add(teacher2Class.getClassOfStudents());
        }
        List<ClassOfStudents> classOfStudentsListSchool = school.getClassOfStudentsList();
        for (ClassOfStudents classOfStudents : classOfStudentsListSchool) {
            if (!classOfStudentsListTeacher.contains(classOfStudents)) {
                result.add(classOfStudents);
            }
        }
        return result;
    }

    public void addClassToTeacher(int teacherId, int classId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> getRuntimeException(teacherId));
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> getRuntimeException(classId));

        Teacher2Class teacher2Class = new Teacher2Class();
        teacher2Class.setTeacher(teacher);
        teacher2Class.setClassOfStudents(classOfStudents);

        teacher2ClassRepository.save(teacher2Class);
    }

    public void deleteClassForTeacher(int teacherId, int classId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> getRuntimeException(teacherId));
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> getRuntimeException(classId));
        List<Teacher2Class> teacher2ClassList = classOfStudents.getTeacher2ClassList();
        for (Teacher2Class teacher2Class : teacher2ClassList) {
            if (teacher2Class.getTeacher().equals(teacher)) {
                teacher2ClassRepository.delete(teacher2Class);
            }
        }
    }


    private RuntimeException getRuntimeException(int id) {
        return new RuntimeException("Cannot find teacher by id " + id);
    }

    private ClassOfStudentsDto setClassOfStudentDto(ClassOfStudents classOfStudents) {
        ClassOfStudentsDto classOfStudentsDto = new ClassOfStudentsDto();
        classOfStudentsDto.setClassId(classOfStudents.getClassId());
        classOfStudentsDto.setClassName(classOfStudents.getClassName());
        classOfStudentsDto.setStudents(classOfStudents.getStudents());
        classOfStudentsDto.setClassMaster(classOfStudentsDto.getClassMaster());
        classOfStudentsDto.setSchoolId(classOfStudents.getSchool().getSchoolId());
        return classOfStudentsDto;
    }

    private Teacher setTeacher(TeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(teacherDto.getTeacherId());
        teacher.setTeacherFirstName(teacherDto.getTeacherFirstName());
        teacher.setTeacherLastName(teacherDto.getTeacherLastName());
        return teacher;
    }

    private TeacherDto setTeacherDto(Teacher teacher) {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setTeacherId(teacher.getTeacherId());
        teacherDto.setTeacherFirstName(teacher.getTeacherFirstName());
        teacherDto.setTeacherLastName(teacher.getTeacherLastName());
        return teacherDto;
    }
}
