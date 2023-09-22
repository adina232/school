package com.example.school.service;

import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.entity.*;
import com.example.school.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;


    public void addStudent(StudentDto studentDto) {
        studentRepository.save(setStudent(studentDto));
    }

    public List<StudentDto> getStudentListDto() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentDto> studentDtoList = new ArrayList<>();
        for (Student student : studentList) {
            studentDtoList.add(setStudentDto(student));
        }
        studentDtoList.sort(new Comparator<StudentDto>() {
            @Override
            public int compare(StudentDto o1, StudentDto o2) {
                return o1.getStudentLastName().compareTo(o2.getStudentLastName());
            }
        });
        return studentDtoList;
    }


    public void deleteStudent(int studentId) {
        studentRepository.deleteById(studentId);
    }

    public Student getStudent(int studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> getRuntimeException(studentId));
    }

    public void modifyStudent(StudentDto studentDto) {
        Student student = studentRepository.findById(studentDto.getStudentId())
                .orElseThrow(() -> getRuntimeException(studentDto.getStudentId()));
        studentRepository.save(setStudent(studentDto));
    }

    public List<SubjectDto> getSubjectList(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> getRuntimeException(studentId));
        List<Subject2Student> subject2StudentList = student.getSubject2StudentList();
        List<SubjectDto> result = new ArrayList<>();
        for (Subject2Student subject2Student : subject2StudentList) {
            result.add(setSubjectDto(subject2Student.getSubject()));
        }
        return result;
    }

    public String getClassMaster(int studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> getRuntimeException(studentId));
        Teacher teacher = student.getClassOfStudents().getClassMaster();
        return teacher.getTeacherFirstName() + " " + teacher.getTeacherLastName();
    }


    private SubjectDto setSubjectDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setSubjectId(subject.getSubjectId());
        subjectDto.setSubjectName(subject.getSubjectName());
        List<Subject2Teacher> subject2TeacherList = subject.getSubject2TeacherList();
        for (Subject2Teacher subject2Teacher : subject2TeacherList) {
            if (subject2Teacher.getSubject().equals(subject)) {
                subjectDto.setTeacher(subject2Teacher.getTeacher().getTeacherLastName() + " "
                        + subject2Teacher.getTeacher().getTeacherFirstName());
            }
        }
        List<Subject2Student> subject2StudentList = subject.getSubject2StudentList();
        for (Subject2Student subject2Student : subject2StudentList) {
            if (subject2Student.getSubject().equals(subject)) {
                subjectDto.setGrade(subject2Student.getGrade());
            }
        }
        return subjectDto;
    }

    private RuntimeException getRuntimeException(int id) {
        return new RuntimeException("Can not find student with id" + id);
    }

    private Student setStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setStudentId(studentDto.getStudentId());
        student.setStudentFirstName(studentDto.getStudentFirstName());
        student.setStudentLastName(studentDto.getStudentLastName());
        student.setStudentDateOfBirth(parseStringToDate(studentDto.getStudentDateOfBirth()));
        student.setClassOfStudents(studentDto.getClassOfStudents());
        student.setSubject2StudentList(studentDto.getSubject2StudentList());
        return student;
    }

    private StudentDto setStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(student.getStudentId());
        studentDto.setStudentFirstName(student.getStudentFirstName());
        studentDto.setStudentLastName(student.getStudentLastName());
        studentDto.setStudentDateOfBirth(String.valueOf(student.getStudentDateOfBirth()));
        return studentDto;
    }

    private Date parseStringToDate(String string) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

}
