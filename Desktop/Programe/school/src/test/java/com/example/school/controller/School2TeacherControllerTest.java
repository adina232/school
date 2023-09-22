package com.example.school.controller;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.School2TeacherDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.service.School2TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class School2TeacherControllerTest {
    private final School2TeacherService school2TeacherService = mock(School2TeacherService.class);
    private final School2TeacherController school2TeacherController =
            new School2TeacherController(school2TeacherService);

    @Test
    void getSchoolsAndTeachers() {
        // data
        Model model = mock(Model.class);
        List<School2TeacherDto> school2TeacherDtoList = new ArrayList<>();
        when(school2TeacherService.getList()).thenReturn(school2TeacherDtoList);
        // run
        String result = school2TeacherController.getSchoolsAndTeachers(model);
        // verify
        verify(model).addAttribute("school2TeacherListDto", school2TeacherDtoList);
        verify(school2TeacherService).getList();
        assertEquals("schoolsAndTeachers", result);
    }

    @Test
    void getSubjectsForSchool2Teacher() {
        // data
        int school2TeacherId = 1;
        Model model = mock(Model.class);
        List<SubjectDto> subject2TeacherList = new ArrayList<>();
        when(school2TeacherService.getSubjectsForSchool2Teacher(school2TeacherId))
                .thenReturn(subject2TeacherList);
        when(school2TeacherService.getSchoolName(school2TeacherId)).thenReturn("school");
        when(school2TeacherService.getTeacherName(school2TeacherId)).thenReturn("teacher");
        // run
        String result = school2TeacherController.getSubjectsForSchool2Teacher(school2TeacherId, model);
        // verify
        verify(model).addAttribute("subjectListForS2T", subject2TeacherList);
        verify(model).addAttribute("school2TeacherId", school2TeacherId);
        verify(model).addAttribute("schoolName", "school");
        verify(model).addAttribute("teacherName", "teacher");
        assertEquals("subjectsForSchool2Teacher", result);
    }

    @Test
    void getClassesForSubjects() {
        // data
        int school2TeacherId = 1;
        int subjectId = 2;
        Model model = mock(Model.class);
        List<ClassOfStudentsDto> classOfStudentsDtoList = new ArrayList<>();
        String schoolName = "school";
        String teacherName = "teacher";
        String subjectName = "subject";
        when(school2TeacherService.getClassesForSubjects(school2TeacherId, subjectId))
                .thenReturn(classOfStudentsDtoList);
        when(school2TeacherService.getSchoolName(school2TeacherId)).thenReturn(schoolName);
        when(school2TeacherService.getTeacherName(school2TeacherId)).thenReturn(teacherName);
        when(school2TeacherService.getSubjectName(subjectId)).thenReturn(subjectName);
        // run
        String result = school2TeacherController.getClassesForSubjects(school2TeacherId, subjectId, model);
        // verify
        verify(model).addAttribute("classListForSubject", classOfStudentsDtoList);
        verify(model).addAttribute("schoolName", "school");
        verify(model).addAttribute("teacherName", "teacher");
        verify(model).addAttribute("subjectName", "subject");
        verify(model).addAttribute("school2TeacherId", 1);
        verify(model).addAttribute("subjectId", 2);
        assertEquals("classesForSchool2Teacher", result);
    }

    @Test
    void deleteClassForSubject() {
        // data
        int school2TeacherId = 1;
        int subjectId = 2;
        int classId = 3;
        Model model = mock(Model.class);
        // run
        String result = school2TeacherController.deleteClassForSubject(classId, subjectId,
                school2TeacherId, model);
        // verify
        verify(school2TeacherService).deleteClassForSubject(classId, subjectId);
        verify(model).addAttribute("subjectId", 2);
        verify(model).addAttribute("school2TeacherId", 1);
        assertEquals("redirect:/classesForSubjects?school2TeacherId=" + school2TeacherId +
                "&subjectId=" + subjectId, result);
    }

    @Test
    void addClassToSubjectList() {
        // data
        int school2TeacherId = 1;
        int subjectId = 2;
        Model model = mock(Model.class);
        List<ClassOfStudentsDto> classOfStudentsDtoList = new ArrayList<>();
        String schoolName = "school";
        String teacherName = "teacher";
        String subjectName = "subject";

        when(school2TeacherService.addClassToSubjectList(school2TeacherId, subjectId))
                .thenReturn(classOfStudentsDtoList);
        when(school2TeacherService.getSchoolName(school2TeacherId)).thenReturn(schoolName);
        when(school2TeacherService.getTeacherName(school2TeacherId)).thenReturn(teacherName);
        when(school2TeacherService.getSubjectName(subjectId)).thenReturn(subjectName);
        // run
        String result = school2TeacherController.addClassToSubjectList(school2TeacherId,
                subjectId, model);
        // verify
        verify(model).addAttribute("classListForSubjectToAdd", classOfStudentsDtoList);
        verify(model).addAttribute("schoolName", "school");
        verify(model).addAttribute("teacherName", "teacher");
        verify(model).addAttribute("subjectName", "subject");
        verify(model).addAttribute("school2TeacherId", 1);
        verify(model).addAttribute("subjectId", 2);
        assertEquals("school2TeacherAddClassToSubject", result);
    }

    @Test
    void addClassToSubject() {
        // data
        int school2TeacherId = 1;
        int subjectId = 2;
        int classId = 3;
        // run
        String result = school2TeacherController.addClassToSubject(school2TeacherId, subjectId, classId);
        // verify
        verify(school2TeacherService).addClassToSubject(subjectId, classId);
        assertEquals("redirect:/addClassToSubjectList?school2TeacherId=" +
                school2TeacherId + "&subjectId=" + subjectId, result);
    }

    @Test
    void getCatalog() {
        // data
        int school2TeacherId = 1;
        int subjectId = 2;
        int classId = 3;
        Model model = mock(Model.class);
        List<StudentDto> studentDtoList = new ArrayList<>();
        String schoolName = "school";
        String teacherName = "teacher";
        String subjectName = "subject";
        String className = "class";

        when(school2TeacherService.getStudentListForCatalog(school2TeacherId, subjectId))
                .thenReturn(studentDtoList);
        when(school2TeacherService.getSchoolName(school2TeacherId)).thenReturn(schoolName);
        when(school2TeacherService.getTeacherName(school2TeacherId)).thenReturn(teacherName);
        when(school2TeacherService.getSubjectName(subjectId)).thenReturn(subjectName);
        when(school2TeacherService.getClassName(classId)).thenReturn(className);
        // run
        String result = school2TeacherController.getCatalog(school2TeacherId, subjectId, classId, model);
        // verify
        verify(model).addAttribute("studentListForCatalog", studentDtoList);
        verify(model).addAttribute("schoolName", "school");
        verify(model).addAttribute("teacherName", "teacher");
        verify(model).addAttribute("subjectName", "subject");
        verify(model).addAttribute("className", "class");
        verify(model).addAttribute("school2TeacherId", 1);
        verify(model).addAttribute("subjectId", 2);
        verify(model).addAttribute("classId", 3);
        assertEquals("school2TeacherCatalog", result);
    }

    @Test
    void addGrade() {
        // data
        int school2TeacherId = 1;
        int subjectId = 2;
        int classId = 3;
        Model model = mock(Model.class);
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentId(1);
        studentDto.setGrade(10);
        // run
        String result = school2TeacherController.addGrade(school2TeacherId, subjectId, classId,
                studentDto, model);
        // verify
        verify(school2TeacherService).addGrade(subjectId, 1, 10);
        verify(model).addAttribute("school2TeacherId", 1);
        verify(model).addAttribute("subjectId", 2);
        verify(model).addAttribute("classId", 3);
        assertEquals("redirect:/catalog?school2TeacherId=" + school2TeacherId +
                "&subjectId=" + subjectId + "&classId=" + classId, result);
    }
}