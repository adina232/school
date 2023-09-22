package com.example.school.service;

import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.entity.*;
import com.example.school.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final StudentService studentService = new StudentService(studentRepository);

    @Test
    void addStudent() {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(1);
        studentDto.setStudentDateOfBirth("2000-01-01");

        Student student = new Student();
        student.setStudentId(1);
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        student.setStudentDateOfBirth(date);

        studentService.addStudent(studentDto);

        verify(studentRepository).save(student);
    }

    @Test
    void getStudentListDto() {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setStudentId(1);
        student.setStudentLastName("i");
        student.setStudentDateOfBirth(parseStringToDate("2000-01-01"));

        Student studentSecond = new Student();
        studentSecond.setStudentId(2);
        studentSecond.setStudentLastName("ii");
        student.setStudentDateOfBirth(parseStringToDate("2000-01-02"));

        studentList.add(studentSecond);
        studentList.add(student);

        when(studentRepository.findAll()).thenReturn(studentList);

        List<StudentDto> actual = studentService.getStudentListDto();

        assertEquals(1, actual.get(0).getStudentId());
        assertEquals(2, actual.get(1).getStudentId());
        assertEquals("i", actual.get(0).getStudentLastName());
        assertEquals("ii", actual.get(1).getStudentLastName());
        assertNull(actual.get(0).getStudentFirstName());
        assertNull(actual.get(1).getStudentFirstName());
        assertNull(actual.get(0).getSubject2StudentList());
        assertNull(actual.get(1).getSubject2StudentList());
        assertEquals(0f, actual.get(0).getGrade());
        assertEquals(0f, actual.get(1).getGrade());
        assertNull(actual.get(0).getClassOfStudents());
        assertNull(actual.get(1).getClassOfStudents());
    }

    @Test
    void deleteStudent() {
        int studentId = 1;
        studentService.deleteStudent(studentId);
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void getStudent() {
        Student student = new Student();
        student.setStudentId(1);

        when(studentRepository.findById(student.getStudentId())).thenReturn(Optional.of(student));

        Student actual = studentService.getStudent(student.getStudentId());

        assertEquals(1, actual.getStudentId());
        assertNull(actual.getStudentLastName());
        assertNull(actual.getStudentFirstName());
        assertNull(actual.getSubject2StudentList());
        assertNull(actual.getStudentDateOfBirth());
        assertNull(actual.getClassOfStudents());
    }

    @Test
    void getStudent_with() {
        int studentId = 1;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    studentService.getStudent(studentId);
                }
        );
        assertEquals("Can not find student with id" + studentId, exception.getMessage());
    }

    @Test
    void modifyStudent() {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(1);
        studentDto.setStudentDateOfBirth("2000-01-01");

        Student student = new Student();
        student.setStudentId(1);
        student.setStudentDateOfBirth(parseStringToDate("2000-01-01"));

        when(studentRepository.findById(studentDto.getStudentId())).thenReturn(Optional.of(student));

        studentService.modifyStudent(studentDto);

        verify(studentRepository).save(student);
    }

    @Test
    void getSubjectList() {
        Student student = new Student();
        student.setStudentId(1);

        Subject2Student subject2Student = new Subject2Student();
        Subject subject = new Subject();
        subject.setSubjectName("test");
        subject.setSubjectId(2);
        subject.setSubject2TeacherList(new ArrayList<>());
        subject2Student.setSubject(subject);
        List<Subject2Student> subject2StudentList = Collections.singletonList(subject2Student);
        subject.setSubject2StudentList(subject2StudentList);

        student.setSubject2StudentList(subject2StudentList);

        when(studentRepository.findById(student.getStudentId())).thenReturn(Optional.of(student));

        List<SubjectDto> actual = studentService.getSubjectList(student.getStudentId());

        assertEquals("test", actual.get(0).getSubjectName());
        assertEquals(2, actual.get(0).getSubjectId());
        assertNull(actual.get(0).getTeacher());
        assertEquals(0f, actual.get(0).getGrade());
    }

    @Test
    void getClassMaster() {
        Student student = new Student();
        student.setStudentId(1);
        ClassOfStudents classOfStudents = new ClassOfStudents();
        Teacher teacher = new Teacher();
        teacher.setTeacherFirstName("first");
        teacher.setTeacherLastName("last");
        classOfStudents.setClassMaster(teacher);
        student.setClassOfStudents(classOfStudents);

        when(studentRepository.findById(student.getStudentId())).thenReturn(Optional.of(student));

        String actual = studentService.getClassMaster(student.getStudentId());

        assertEquals("first last", actual);
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