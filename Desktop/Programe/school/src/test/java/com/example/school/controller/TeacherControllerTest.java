package com.example.school.controller;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.SchoolDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.ClassOfStudents;
import com.example.school.entity.School;
import com.example.school.entity.Teacher;
import com.example.school.service.SchoolService;
import com.example.school.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherControllerTest {
    private final TeacherService teacherService = mock(TeacherService.class);
    private final SchoolService schoolService = mock(SchoolService.class);
    private final TeacherController teacherController = new TeacherController(teacherService,
            schoolService);

    @Test
    void addTeacher() {
        TeacherDto teacherDto = new TeacherDto();
        String actual = teacherController.addTeacher(teacherDto);
        verify(teacherService).addTeacher(teacherDto);
        assertEquals("redirect:/teachers", actual);
    }

    @Test
    void getTeacherForm() {
        ModelAndView actual = teacherController.getTeacherForm();
        assertEquals("/teacher/teacherForm", actual.getViewName());
        assertTrue(actual.getModel().containsKey("teacher"));

        Teacher teacher = (Teacher) actual.getModel().get("teacher");

        assertEquals(0, teacher.getTeacherId());
        assertNull(teacher.getTeacherLastName());
        assertNull(teacher.getTeacherFirstName());
        assertNull(teacher.getSubject2TeacherList());
        assertNull(teacher.getSchool2TeacherList());
        assertNull(teacher.getTeacher2ClassList());
        assertNull(teacher.getClassOfStudents());
    }

    @Test
    void getTeachers() {
        Model model = mock(Model.class);
        List<TeacherDto> teacherDtoList = Collections.singletonList(new TeacherDto());
        when(teacherService.getTeachers()).thenReturn(teacherDtoList);

        String actual = teacherController.getTeachers(model);

        verify(model).addAttribute("teachers", teacherDtoList);
        assertEquals("teacher/teachers", actual);
    }

    @Test
    void deleteTeacher() {
        int teacherId = 1;
        String actual = teacherController.deleteTeacher(teacherId);
        verify(teacherService).deleteTeacher(teacherId);
        assertEquals("redirect:/teachers", actual);
    }

    @Test
    void modifyTeacher() {
        Model model = mock(Model.class);

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setTeacherId(1);
        teacherDto.setTeacherLastName("last");
        teacherDto.setTeacherFirstName("first");

        when(teacherService.getTeacherDto(teacherDto.getTeacherId())).thenReturn(teacherDto);
        when(teacherService.getTeacherFirstName(teacherDto.getTeacherId()))
                .thenReturn(teacherDto.getTeacherFirstName());
        when(teacherService.getTeacherLastName(teacherDto.getTeacherId()))
                .thenReturn(teacherDto.getTeacherLastName());

        String actual = teacherController.modifyTeacher(teacherDto.getTeacherId(), model);

        verify(model).addAttribute("teacherDto", teacherDto);
        verify(model).addAttribute("teacherFirstName", "first");
        verify(model).addAttribute("teacherLastName", "last");
        assertEquals("teacher/teacherEdit", actual);
    }

    @Test
    void editTeacher() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setTeacherId(1);

        String actual = teacherController.editTeacher(teacherDto);

        verify(teacherService).editTeacher(teacherDto);
        assertEquals("redirect:/teachers", actual);
    }

    @Test
    void getTeacherSeesSchools() {
        Model model = mock(Model.class);

        Teacher teacher = new Teacher();
        teacher.setTeacherFirstName("first");
        teacher.setTeacherLastName("last");
        teacher.setTeacherId(1);
        List<School> schoolList = Collections.singletonList(new School());

        when(teacherService.getTeacherSchools(teacher.getTeacherId())).thenReturn(schoolList);
        when(teacherService.getTeacherFirstName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherFirstName());
        when(teacherService.getTeacherLastName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherLastName());

        String actual = teacherController.getTeacherSeesSchools(teacher.getTeacherId(), model);

        verify(model).addAttribute("teacherSchools", schoolList);
        verify(model).addAttribute("teacherId", 1);
        verify(model).addAttribute("teacherFirstName", "first");
        verify(model).addAttribute("teacherLastName", "last");
        assertEquals("teacher/teacherSeesSchools", actual);
    }

    @Test
    void getAddSchoolsList() {
        Model model = mock(Model.class);

        Teacher teacher = new Teacher();
        teacher.setTeacherFirstName("first");
        teacher.setTeacherLastName("last");
        teacher.setTeacherId(1);
        List<School> schoolList = Collections.singletonList(new School());

        when(teacherService.getSchoolsForTeacherToAdd(teacher.getTeacherId())).thenReturn(schoolList);
        when(teacherService.getTeacherFirstName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherFirstName());
        when(teacherService.getTeacherLastName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherLastName());

        String actual = teacherController.getAddSchoolsList(teacher.getTeacherId(), model);

        verify(model).addAttribute("schoolsForTeacherToAdd", schoolList);
        verify(model).addAttribute("teacherId", 1);
        verify(model).addAttribute("teacherFirstName", "first");
        verify(model).addAttribute("teacherLastName", "last");
        assertEquals("teacher/teacherAddsSchools", actual);
    }

    @Test
    void addSchoolToTeacher() {
        int teacherId = 1;
        int schoolId = 2;

        String actual = teacherController.addSchoolToTeacher(teacherId, schoolId);

        verify(teacherService).addSchoolToTeacher(teacherId, schoolId);
        assertEquals("redirect:/addSchoolsList?teacherId=" + teacherId, actual);
    }

    @Test
    void deleteSchoolFromTeacher() {
        int teacherId = 1;
        int schoolId = 2;

        String actual = teacherController.deleteSchoolFromTeacher(teacherId, schoolId);

        verify(teacherService).deleteSchoolFromTeacher(teacherId, schoolId);
        assertEquals("redirect:/teacherSeesSchools?teacherId=" + teacherId, actual);
    }

    @Test
    void getClassesForTeacher() {
        Model model = mock(Model.class);

        Teacher teacher = new Teacher();
        teacher.setTeacherFirstName("first");
        teacher.setTeacherLastName("last");
        teacher.setTeacherId(1);

        SchoolDto school = new SchoolDto();
        school.setSchoolId(2);
        school.setSchoolName("test");

        List<ClassOfStudentsDto> classesList = Collections.singletonList(new ClassOfStudentsDto());

        when(teacherService.getListOfClassesForTeacher(teacher.getTeacherId()))
                .thenReturn(classesList);
        when(schoolService.getSchool(school.getSchoolId())).thenReturn(school);
        when(teacherService.getTeacherFirstName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherFirstName());
        when(teacherService.getTeacherLastName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherLastName());

        String actual = teacherController.getClassesForTeacher(teacher.getTeacherId(),
                school.getSchoolId(), model);

        verify(model).addAttribute("classesList", classesList);
        verify(model).addAttribute("schoolName", "test");
        verify(model).addAttribute("teacherFirstName", "first");
        verify(model).addAttribute("teacherLastName", "last");
        verify(model).addAttribute("teacherId", 1);
        verify(model).addAttribute("schoolId", 2);
        assertEquals("/teacher/teacherSeeClasses", actual);
    }

    @Test
    void getClassesForTeacherToAdd() {
        Model model = mock(Model.class);

        Teacher teacher = new Teacher();
        teacher.setTeacherFirstName("first");
        teacher.setTeacherLastName("last");
        teacher.setTeacherId(1);

        SchoolDto school = new SchoolDto();
        school.setSchoolId(2);
        school.setSchoolName("test");

        List<ClassOfStudents> classListForTeacher = Collections.singletonList(new ClassOfStudents());

        when(teacherService.getClassListForTeacherToAdd(school.getSchoolId(), teacher.getTeacherId()))
                .thenReturn(classListForTeacher);
        when(schoolService.getSchool(school.getSchoolId())).thenReturn(school);
        when(teacherService.getTeacherFirstName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherFirstName());
        when(teacherService.getTeacherLastName(teacher.getTeacherId()))
                .thenReturn(teacher.getTeacherLastName());

        String actual = teacherController.getClassesForTeacherToAdd(teacher.getTeacherId(),
                school.getSchoolId(), model);

        verify(model).addAttribute("classListForTeacher", classListForTeacher);
        verify(model).addAttribute("schoolName", "test");
        verify(model).addAttribute("teacherFirstName", "first");
        verify(model).addAttribute("teacherLastName", "last");
        verify(model).addAttribute("teacherId", 1);
        verify(model).addAttribute("schoolId", 2);
        assertEquals("/teacher/teacherAddClasses", actual);
    }

    @Test
    void addClassToTeacher() {
        int teacherId = 1;
        int schoolId = 2;
        int classId = 3;
        String actual = teacherController.addClassToTeacher(teacherId, schoolId, classId);
        verify(teacherService).addClassToTeacher(teacherId, classId);
        assertEquals("redirect:/addClassesToTeacherList?teacherId=" + teacherId +
                "&schoolId=" + schoolId, actual);
    }

    @Test
    void deleteClassForTeacher() {
        int teacherId = 1;
        int schoolId = 2;
        int classId = 3;
        String actual = teacherController.deleteClassForTeacher(teacherId, schoolId, classId);
        verify(teacherService).deleteClassForTeacher(teacherId, classId);
        assertEquals("redirect:/getClassesForTeacher?teacherId=" + teacherId +
                "&schoolId=" + schoolId, actual);
    }
}