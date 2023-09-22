package com.example.school.controller;

import com.example.school.dto.SubjectDto;
import com.example.school.entity.Subject;
import com.example.school.entity.Teacher;
import com.example.school.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectControllerTest {
    private final SubjectService subjectService = mock(SubjectService.class);
    private final SubjectController subjectController = new SubjectController(subjectService);

    @Test
    void getSubjectPage() {
        String actual = subjectController.getSubjectPage();
        assertEquals("subject/subjectHome", actual);
    }

    @Test
    void addSubjectForm() {
        ModelAndView actual = subjectController.addSubjectForm();
        assertEquals("/subject/subjectForm", actual.getViewName());
        assertTrue(actual.getModel().containsKey("subject"));

        Subject subject = (Subject) actual.getModel().get("subject");

        assertEquals(0, subject.getSubjectId());
        assertNull(subject.getSubjectName());
        assertNull(subject.getSubject2TeacherList());
        assertNull(subject.getSubject2ClassList());
        assertNull(subject.getSubject2StudentList());
    }

    @Test
    void addSubject() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setSubjectId(1);
        subjectDto.setSubjectName("test");

        String actual = subjectController.addSubject(subjectDto);

        verify(subjectService).addSubject(subjectDto);
        assertEquals("redirect:/subjects", actual);
    }

    @Test
    void getSubjects() {
        Model model = mock(Model.class);
        SubjectDto subjectDto = new SubjectDto();
        List<SubjectDto> subjectDtoList = Collections.singletonList(subjectDto);
        when(subjectService.getSubjects()).thenReturn(subjectDtoList);

        String actual = subjectController.getSubjects(model);

        verify(model).addAttribute("subjectList", subjectDtoList);
        assertEquals("subject/subjects", actual);
    }

    @Test
    void subjectSeeTeachers() {
        int subjectId = 1;
        String subjectName = "test";
        Model model = mock(Model.class);
        Teacher teacher = new Teacher();
        List<Teacher> teacherList = Collections.singletonList(teacher);
        when(subjectService.getTeachersForSubject(subjectId)).thenReturn(teacherList);
        when(subjectService.getSubjectName(subjectId)).thenReturn(subjectName);

        String actual = subjectController.subjectSeeTeachers(subjectId, model);

        verify(model).addAttribute("teacherList", teacherList);
        verify(model).addAttribute("subjectId", subjectId);
        verify(model).addAttribute("subjectName", subjectName);
        assertEquals("subject/subjectSeeTeachers", actual);
    }

    @Test
    void subjectSeeTeachersToAdd() {
        int subjectId = 1;
        String subjectName = "test";
        Model model = mock(Model.class);
        Teacher teacher = new Teacher();
        List<Teacher> teacherList = Collections.singletonList(teacher);
        when(subjectService.getTeachersToAdd(subjectId)).thenReturn(teacherList);
        when(subjectService.getSubjectName(subjectId)).thenReturn(subjectName);

        String actual = subjectController.subjectSeeTeachersToAdd(subjectId, model);

        verify(model).addAttribute("teachersToAdd", teacherList);
        verify(model).addAttribute("subjectId", subjectId);
        verify(model).addAttribute("subjectName", subjectName);
        assertEquals("subject/subjectAddTeacher", actual);
    }

    @Test
    void subjectAddTeacher() {
        int subjectId = 1;
        int teacherId = 2;

        String actual = subjectController.subjectAddTeacher(subjectId, teacherId);

        verify(subjectService).addTeacherToSubject(subjectId, teacherId);
        assertEquals("redirect:/subjectSeeTeachers?subjectId=" + subjectId, actual);
    }

    @Test
    void deleteSubject() {
        int subjectId = 1;
        String actual = subjectController.deleteSubject(subjectId);
        verify(subjectService).deleteSubject(subjectId);
        assertEquals("redirect:/subjects", actual);
    }

    @Test
    void deleteTeacherFromSubject() {
        int subjectId = 1;
        int teacherId = 2;
        String actual = subjectController.deleteTeacherFromSubject(subjectId, teacherId);
        verify(subjectService).deleteTeacherFromSubject(subjectId, teacherId);
        assertEquals("redirect:/subjectSeeTeachers?subjectId=" + subjectId, actual);
    }
}