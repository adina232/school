package com.example.school.controller;

import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.entity.ClassOfStudents;
import com.example.school.entity.Student;
import com.example.school.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    private final StudentService studentService = mock(StudentService.class);
    private final StudentController studentController = new StudentController(studentService);

    @Test
    void getStudentHome() {
        String actual = studentController.getStudentHome();

        assertEquals("/student/studentHome", actual);
    }

    @Test
    void getStudentForm() {
        Model model = mock(Model.class);

        String actual = studentController.getStudentForm(model);

        verify(model).addAttribute("student", new Student());
        assertEquals("/student/studentForm", actual);
    }

    @Test
    void addStudent() {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(1);
        studentDto.setStudentFirstName("test");

        String actual = studentController.addStudent(studentDto);

        verify(studentService).addStudent(studentDto);
        assertEquals("redirect:/students", actual);
    }

    @Test
    void getStudentList() {
        Model model = mock(Model.class);
        List<StudentDto> studentDtoList = new ArrayList<>();

        when(studentService.getStudentListDto()).thenReturn(studentDtoList);

        String actual = studentController.getStudentList(model);

        verify(model).addAttribute("studentListDto", studentDtoList);
        assertEquals("/student/students", actual);
    }

    @Test
    void deleteStudent() {
        int studentId = 1;

        String actual = studentController.deleteStudent(studentId);

        verify(studentService).deleteStudent(studentId);
        assertEquals("redirect:/students", actual);
    }

    @Test
    void modifyStudentForm() {
        Student student = new Student();
        student.setStudentId(1);
        student.setStudentLastName("last");
        student.setStudentFirstName("first");
        Model model = mock(Model.class);

        when(studentService.getStudent(student.getStudentId())).thenReturn(student);

        String actual = studentController.modifyStudentForm(student.getStudentId(), model);

        verify(model).addAttribute("student", student);
        verify(model).addAttribute("studentLastName", "last");
        verify(model).addAttribute("studentFirstName", "first");
        assertEquals("student/studentModify", actual);
    }

    @Test
    void modifyStudent() {
        StudentDto studentDto = new StudentDto();

        String actual = studentController.modifyStudent(studentDto);

        verify(studentService).modifyStudent(studentDto);
        assertEquals("redirect:/students", actual);
    }

    @Test
    void getCatalogForStudents() {
        Model model = mock(Model.class);

        Student student = new Student();
        student.setStudentId(1);
        student.setStudentLastName("last");
        student.setStudentFirstName("first");

        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassName("test");
        student.setClassOfStudents(classOfStudents);

        SubjectDto subjectDto = new SubjectDto();
        List<SubjectDto> subjectDtoList = Collections.singletonList(subjectDto);

        when(studentService.getStudent(student.getStudentId())).thenReturn(student);
        when(studentService.getClassMaster(student.getStudentId())).thenReturn("first last");
        when(studentService.getSubjectList(student.getStudentId())).thenReturn(subjectDtoList);

        String actual = studentController.getCatalogForStudents(student.getStudentId(), model);

        verify(model).addAttribute("studentLastName", "last");
        verify(model).addAttribute("studentFirstName", "first");
        verify(model).addAttribute("classMaster", "first last");
        verify(model).addAttribute("className", "test");
        verify(model).addAttribute("subjectList", subjectDtoList);
        assertEquals("student/studentCatalog", actual);
    }
}