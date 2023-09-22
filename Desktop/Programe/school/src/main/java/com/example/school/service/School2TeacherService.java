package com.example.school.service;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.School2TeacherDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.entity.*;
import com.example.school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class School2TeacherService {
    private final SchoolRepository schoolRepository;
    private final ClassOfStudentsRepository classOfStudentsRepository;
    private final SubjectRepository subjectRepository;
    private final Subject2ClassRepository subject2ClassRepository;
    private final School2TeacherRepository school2TeacherRepository;
    private final Subject2StudentRepository subject2StudentRepository;
    private final StudentRepository studentRepository;

    public List<School2TeacherDto> getList() {
        List<School> schoolList = schoolRepository.findAll();
        List<School2TeacherDto> resultList = new ArrayList<>();
        for (School school : schoolList) {
            List<School2Teacher> school2TeacherList = school.getSchool2TeacherList();
            for (School2Teacher school2Teacher : school2TeacherList) {
                School2TeacherDto result = new School2TeacherDto();
                result.setId(school2Teacher.getId());
                result.setSchoolName(school.getSchoolName());
                result.setTeacherFirstName(school2Teacher.getTeacher().getTeacherFirstName());
                result.setTeacherLastName(school2Teacher.getTeacher().getTeacherLastName());
                resultList.add(result);
            }
        }
        resultList.sort(new Comparator<School2TeacherDto>() {
            @Override
            public int compare(School2TeacherDto o1, School2TeacherDto o2) {
                return o1.getSchoolName().compareTo(o2.getSchoolName());
            }
        });
        return resultList;
    }

    public List<SubjectDto> getSubjectsForSchool2Teacher(int school2TeacherId) {
        Optional<School2Teacher> school2Teacher = school2TeacherRepository.findById(school2TeacherId);
        Teacher teacher = school2Teacher.get().getTeacher();
        List<Subject2Teacher> subject2TeacherList = teacher.getSubject2TeacherList();
        List<SubjectDto> subjectListDto = new ArrayList<>();
        for (Subject2Teacher subject2Teacher : subject2TeacherList) {
            subjectListDto.add(setSubjectDto(subject2Teacher.getSubject()));
        }
        Collections.sort(subjectListDto, new Comparator<SubjectDto>() {
            @Override
            public int compare(SubjectDto s1, SubjectDto s2) {
                return s1.getSubjectName().compareTo(s2.getSubjectName());
            }
        });
        return subjectListDto;
    }

    public List<ClassOfStudentsDto> getClassesForSubjects(int school2TeacherId, int subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> getRuntimeException(subjectId));
        School2Teacher school2Teacher = school2TeacherRepository.findById(school2TeacherId)
                .orElseThrow(() -> getRuntimeException(school2TeacherId));

        Teacher teacher = school2Teacher.getTeacher();
        List<ClassOfStudentsDto> result = new ArrayList<>();

        List<Subject2Teacher> subject2TeacherList = teacher.getSubject2TeacherList();
        List<Subject2Class> subject2ClassList = subject.getSubject2ClassList();

        for (Subject2Teacher subject2Teacher : subject2TeacherList) {
            for (Subject2Class subject2Class : subject2ClassList) {
                if (subject2Teacher.getSubject().equals(subject) &&
                        subject2Class.getSubject().equals(subject)) {
                    result.add(setClassOfStudentsDto(subject2Class.getClassOfStudents()));
                }
            }
        }
        return result;
    }

    public List<ClassOfStudentsDto> addClassToSubjectList(int school2TeacherId, int subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> getRuntimeException(subjectId));
        School2Teacher school2Teacher = school2TeacherRepository.findById(school2TeacherId)
                .orElseThrow(() -> getRuntimeException(school2TeacherId));

        Teacher teacher = school2Teacher.getTeacher();
        List<ClassOfStudents> classOfStudentsList = classOfStudentsRepository.findAll();
        List<ClassOfStudentsDto> result = new ArrayList<>();
        for (ClassOfStudents classOfStudents : classOfStudentsList) {
            if (classOfStudents.getSchool().getSchoolId() == school2Teacher.getSchool().getSchoolId()) {
                result.add(setClassOfStudentsDto(classOfStudents));
            }
        }

        List<Subject2Teacher> subject2TeacherList = teacher.getSubject2TeacherList();
        List<Subject2Class> subject2ClassList = subject.getSubject2ClassList();

        for (Subject2Teacher subject2Teacher : subject2TeacherList) {
            for (Subject2Class subject2Class : subject2ClassList) {
                if (subject2Teacher.getSubject().getSubjectName().equals(
                        subject2Class.getSubject().getSubjectName())) {
                    result.remove(setClassOfStudentsDto(subject2Class.getClassOfStudents()));
                }
            }
        }
        return result;
    }

    public void deleteClassForSubject(int classId, int subjectId) {
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> getRuntimeException(classId));
        List<Subject2Class> subject2ClassList = classOfStudents.getSubject2ClassList();
        for (Subject2Class subject2Class : subject2ClassList) {
            if (subject2Class.getSubject().getSubjectId() == subjectId) {
                subject2ClassRepository.delete(subject2Class);
            }
        }
    }

    public void addClassToSubject(int subjectId, int classId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> getRuntimeException(subjectId));
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> getRuntimeException(classId));
        Subject2Class subject2Class = new Subject2Class();
        subject2Class.setClassOfStudents(classOfStudents);
        subject2Class.setSubject(subject);
        subject2ClassRepository.save(subject2Class);
    }

    public List<StudentDto> getStudentListForCatalog(int subjectId, int classId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> getRuntimeException(subjectId));
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> getRuntimeException(classId));

        List<StudentDto> result = new ArrayList<>();

        List<Subject2Class> subject2ClassList = subject.getSubject2ClassList();
        for (Subject2Class subject2Class : subject2ClassList) {
            if (subject2Class.getClassOfStudents().equals(classOfStudents)) {
                List<Student> studentList = subject2Class.getClassOfStudents().getStudents();
                for (Student student : studentList) {
                    StudentDto studentDto = setStudentDto(student);
                    List<Subject2Student> subject2StudentList = studentDto.getSubject2StudentList();
                    for (Subject2Student subject2Student : subject2StudentList) {
                        if (subject2Student.getSubject().equals(subject)) {
                            studentDto.setGrade(subject2Student.getGrade());

                        }
                    }
                    result.add(studentDto);
                }
            }
        }
        return result;
    }

    public void addGrade(int subjectId, int studentId, float grade) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> getRuntimeException(subjectId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> getRuntimeException(studentId));

        Subject2Student subject2Student = new Subject2Student();
        subject2Student.setGrade(grade);
        subject2Student.setSubject(subject);
        subject2Student.setStudent(student);
        subject2StudentRepository.save(subject2Student);
    }


    public float getGrade(int subjectId, int studentId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> getRuntimeException(subjectId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> getRuntimeException(studentId));
        List<Subject2Student> subject2StudentList = subject.getSubject2StudentList();
        for (Subject2Student subject2Student : subject2StudentList) {
            if (subject2Student.getStudent().equals(student)) {
                return subject2Student.getGrade();
            }
        }
        return 0;
    }

    public String getSubjectName(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> getRuntimeException(subjectId));
        return subject.getSubjectName();
    }

    public String getTeacherName(int school2TeacherId) {
        School2Teacher school2Teacher = school2TeacherRepository.findById(school2TeacherId)
                .orElseThrow(() -> getRuntimeException(school2TeacherId));
        return school2Teacher.getTeacher().getTeacherFirstName() + " " +
                school2Teacher.getTeacher().getTeacherLastName();
    }

    public String getSchoolName(int school2TeacherId) {
        School2Teacher school2Teacher = school2TeacherRepository.findById(school2TeacherId)
                .orElseThrow(() -> getRuntimeException(school2TeacherId));
        return school2Teacher.getSchool().getSchoolName();
    }

    public String getClassName(int classId) {
        ClassOfStudents classOfStudents = classOfStudentsRepository.findById(classId)
                .orElseThrow(() -> getRuntimeException(classId));
        return classOfStudents.getClassName();
    }


    private StudentDto setStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(student.getStudentId());
        studentDto.setStudentLastName(student.getStudentLastName());
        studentDto.setStudentFirstName(student.getStudentFirstName());
        studentDto.setStudentDateOfBirth(String.valueOf(student.getStudentDateOfBirth()));
        studentDto.setClassOfStudents(student.getClassOfStudents());
        studentDto.setSubject2StudentList(student.getSubject2StudentList());
        return studentDto;
    }

    private RuntimeException getRuntimeException(int school2TeacherId) {
        return new RuntimeException("Cannot find school2Teacher with id " + school2TeacherId);
    }

    private ClassOfStudentsDto setClassOfStudentsDto(ClassOfStudents classOfStudents) {
        ClassOfStudentsDto classOfStudentsDto = new ClassOfStudentsDto();
        classOfStudentsDto.setClassId(classOfStudents.getClassId());
        classOfStudentsDto.setClassName(classOfStudents.getClassName());
        return classOfStudentsDto;
    }

    private SubjectDto setSubjectDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setSubjectId(subject.getSubjectId());
        subjectDto.setSubjectName(subject.getSubjectName());
        return subjectDto;
    }

}
