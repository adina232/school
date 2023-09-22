package com.example.school.controller;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.SchoolDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.ClassOfStudents;
import com.example.school.entity.School;
import com.example.school.entity.Teacher;
import com.example.school.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchoolControllerTest {
    private final SchoolService schoolService = mock(SchoolService.class);
    private final SchoolController schoolController = new SchoolController(schoolService);

    @Test
    void home() {
        String result = schoolController.home();
        assertEquals("home", result);
    }

    @Test
    void getSchoolForm() {
        ModelAndView result = schoolController.getSchoolForm();

        assertEquals("/school/schoolForm", result.getViewName());
        assertTrue(result.getModel().containsKey("school"));

        School school = (School) result.getModel().get("school");
        assertEquals(0, school.getSchoolId());
        assertNull(school.getSchoolName());
        assertNull(school.getSchoolAddress());
        assertNull(school.getSchool2TeacherList());
        assertNull(school.getClassOfStudentsList());
    }

    @Test
    void addSchool() {
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolId(1);
        schoolDto.setSchoolName("School");
        schoolDto.setSchoolAddress("Test");
        Model model = mock(Model.class);

        String actual = schoolController.addSchool(schoolDto);

        verify(schoolService).addSchool(schoolDto);
        assertEquals("redirect:/schools", actual);
    }

    @Test
    void getSchools() {
        Model model = mock(Model.class);
        List<SchoolDto> schoolDtoList = new ArrayList<>();
        when(schoolService.getSchools()).thenReturn(schoolDtoList);

        String actual = schoolController.getSchools(model);

        verify(model).addAttribute("schools", new ArrayList<>());
        assertEquals("school/schools", actual);
    }

    @Test
    void deleteSchool() {
        int schoolId = 1;

        String actual = schoolController.deleteSchool(schoolId);

        verify(schoolService).deleteSchool(1);
        assertEquals("redirect:/schools", actual);
    }

    @Test
    void modifySchool() {
        int schoolId = 1;
        Model model = mock(Model.class);
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolName("school");
        schoolDto.setSchoolId(2);

        when(schoolService.getSchool(schoolId)).thenReturn(schoolDto);

        String actual = schoolController.modifySchool(schoolId, model);

        verify(model).addAttribute("schoolDto", schoolDto);
        verify(model).addAttribute("schoolName", "school");
        assertEquals("school/schoolEdit", actual);
    }

    @Test
    void editSchool() {
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolName("school");
        schoolDto.setSchoolId(1);
        schoolDto.setSchoolAddress("test");

        String actual = schoolController.editSchool(schoolDto);

        verify(schoolService).editSchool(schoolDto);
        assertEquals(1, schoolDto.getSchoolId());
        assertEquals("school", schoolDto.getSchoolName());
        assertEquals("test", schoolDto.getSchoolAddress());

        assertEquals("redirect:/schools", actual);
    }

    @Test
    void seeTeachersForSchool() {
        int schoolId = 1;
        Model model = mock(Model.class);
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolId(1);
        schoolDto.setSchoolName("school");
        schoolDto.setSchoolAddress("test");
        when(schoolService.getTeachersInSchool(schoolId)).thenReturn(teacherDtoList);
        when(schoolService.getSchool(schoolId)).thenReturn(schoolDto);

        String actual = schoolController.seeTeachersForSchool(schoolId, model);

        verify(model).addAttribute("teacherDtoList", teacherDtoList);
        verify(model).addAttribute("schoolId", 1);
        verify(model).addAttribute("schoolName", "school");
        assertEquals("school/schoolSeeTeachers", actual);
    }

    @Test
    void deleteTeacherFromSeeTeachers() {
        int teacherId = 1;
        int schoolId = 2;

        String actual = schoolController.deleteTeacherFromSeeTeachers(teacherId, schoolId);

        verify(schoolService).deleteTeacherFromSeeTeachers(1, 2);
        assertEquals("redirect:/seeTeachersForSchool?schoolId=" + schoolId, actual);
    }

    @Test
    void getTeachersForSchool() {
        int schoolId = 1;
        Model model = mock(Model.class);
        List<Teacher> teacherList = new ArrayList<>();
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolId(1);
        schoolDto.setSchoolName("school");
        schoolDto.setSchoolAddress("test");
        when(schoolService.getTeachersForSchool(schoolId)).thenReturn(teacherList);
        when(schoolService.getSchool(schoolId)).thenReturn(schoolDto);

        String actual = schoolController.getTeachersForSchool(schoolId, model);

        verify(model).addAttribute("teacherListForSchool", teacherList);
        verify(model).addAttribute("schoolId", 1);
        verify(model).addAttribute("schoolName", "school");
        assertEquals("school/schoolAddTeacher", actual);
    }

    @Test
    void addTeacherToSchool() {
        int schoolId = 1;
        int teacherId = 2;

        String actual = schoolController.addTeacherToSchool(schoolId, teacherId);

        verify(schoolService).addTeacherToSchool(schoolId, teacherId);
        assertEquals("redirect:/schoolAddTeacher?schoolId=" + schoolId, actual);
    }

    @Test
    void getClassList() {
        int schoolId = 1;
        Model model = mock(Model.class);
        List<ClassOfStudentsDto> classOfStudentsDtoList = new ArrayList<>();
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolId(1);
        schoolDto.setSchoolName("school");
        schoolDto.setSchoolAddress("test");
        when(schoolService.getClassListForSchool(schoolId)).thenReturn(classOfStudentsDtoList);
        when(schoolService.getSchool(schoolId)).thenReturn(schoolDto);

        String actual = schoolController.getClassList(schoolId, model);

        verify(model).addAttribute("classListForSchool", classOfStudentsDtoList);
        verify(model).addAttribute("schoolId", 1);
        verify(model).addAttribute("schoolName", "school");
        assertEquals("school/classes", actual);
    }

    @Test
    void addClass() {
        int schoolId = 1;
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        when(schoolService.getTeachersOptions()).thenReturn(teacherDtoList);

        ModelAndView actual = schoolController.addClass(schoolId);

        assertEquals("/school/addClassToSchool", actual.getViewName());
        assertTrue(actual.getModel().containsKey("classOfStudents"));
        assertEquals(new ClassOfStudents(), actual.getModel().get("classOfStudents"));
        assertTrue(actual.getModel().containsKey("options"));
        assertTrue(actual.getModel().containsKey("schoolId"));
        assertEquals(1, actual.getModel().get("schoolId"));
    }

    @Test
    void addClassToSchool() {
        ClassOfStudentsDto classOfStudentsDto = new ClassOfStudentsDto();
        classOfStudentsDto.setClassId(1);
        classOfStudentsDto.setSchoolId(2);

        String actual = schoolController.addClassToSchool(classOfStudentsDto);

        verify(schoolService).addClassOfStudents(classOfStudentsDto);
        assertEquals("redirect:/classesForSchool?schoolId=" + classOfStudentsDto.getSchoolId(),
                actual);
    }

    @Test
    void deleteClassForSchool() {
        int classId = 1;
        int schoolId = 2;
        when(schoolService.getSchoolId(classId)).thenReturn(schoolId);

        String actual = schoolController.deleteClassForSchool(classId);

        verify(schoolService).deleteClassForSchool(classId);
        assertEquals("redirect:/classesForSchool?schoolId=" + schoolId, actual);
    }

    @Test
    void getStudentsInClass() {
        int classId = 1;
        Model model = mock(Model.class);
        List<StudentDto> studentDtoList = new ArrayList<>();
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        classOfStudents.setClassName("test");
        School school = new School();
        school.setSchoolId(0);
        when(schoolService.getStudentsInClass(classId)).thenReturn(studentDtoList);
        when(schoolService.getClassName(classId)).thenReturn(classOfStudents.getClassName());
        when(schoolService.getSchoolId(classId)).thenReturn(school.getSchoolId());

        String actual = schoolController.getStudentsInClass(classId, model);

        verify(model).addAttribute("studentListInClass", studentDtoList);
        verify(model).addAttribute("className", "test");
        verify(model).addAttribute("classId", 1);
        verify(model).addAttribute("schoolId", 0);
        assertEquals("school/studentsInClass", actual);
    }

    @Test
    void deleteStudentForClass() {
        int classId = 1;
        int studentId = 2;

        String actual = schoolController.deleteStudentForClass(classId, studentId);

        verify(schoolService).deleteStudentForClass(studentId);
        assertEquals("redirect:/studentsInClass?classId=" + classId, actual);
    }

    @Test
    void addStudentsToClassList() {
        int classId = 1;
        Model model = mock(Model.class);
        List<StudentDto> studentDtoList = new ArrayList<>();
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        classOfStudents.setClassName("test");
        when(schoolService.getListOfStudentsToAddToClass(classId)).thenReturn(studentDtoList);
        when(schoolService.getClassName(classId)).thenReturn(classOfStudents.getClassName());

        String actual = schoolController.addStudentsToClassList(classId, model);

        verify(model).addAttribute("studentsToAddToClassList", studentDtoList);
        verify(model).addAttribute("className", "test");
        verify(model).addAttribute("classId", 1);
        assertEquals("school/classAddStudent", actual);
    }

    @Test
    void addStudentToClass() {
        int classId = 1;
        int studentId = 2;

        String actual = schoolController.addStudentToClass(classId, studentId);

        verify(schoolService).addStudentToClass(classId, studentId);
        assertEquals("redirect:/addStudentsToClassList?classId=" + classId, actual);
    }
}