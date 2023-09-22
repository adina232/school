package com.example.school.service;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.*;
import com.example.school.repository.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {
    private final TeacherRepository teacherRepository = mock(TeacherRepository.class);
    private final SchoolRepository schoolRepository = mock(SchoolRepository.class);
    private final School2TeacherRepository school2TeacherRepository =
            mock(School2TeacherRepository.class);
    private final ClassOfStudentsRepository classOfStudentsRepository =
            mock(ClassOfStudentsRepository.class);
    private final Teacher2ClassRepository teacher2ClassRepository = mock(Teacher2ClassRepository.class);
    private final TeacherService teacherService = new TeacherService(teacherRepository, schoolRepository,
            school2TeacherRepository, classOfStudentsRepository, teacher2ClassRepository);

    @Test
    void addTeacher() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setTeacherId(1);

        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);

        teacherService.addTeacher(teacherDto);

        verify(teacherRepository).save(teacher);
    }

    @Test
    void getTeachers() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        List<Teacher> teacherList = Collections.singletonList(teacher);
        when(teacherRepository.findAll()).thenReturn(teacherList);

        List<TeacherDto> actual = teacherService.getTeachers();

        assertEquals(1, actual.get(0).getTeacherId());
        assertNull(actual.get(0).getTeacherLastName());
        assertNull(actual.get(0).getTeacherFirstName());
        assertNull(actual.get(0).getSchool2TeacherList());
        assertNull(actual.get(0).getSubject2TeacherList());
        assertNull(actual.get(0).getSubjects());
    }

    @Test
    void deleteTeacher() {
        int teacherId = 1;
        teacherService.deleteTeacher(teacherId);
        verify(teacherRepository).deleteById(teacherId);
    }

    @Test
    void getTeacherDto() {
        int teacherId = 1;
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        TeacherDto actual = teacherService.getTeacherDto(teacherId);

        assertEquals(1, actual.getTeacherId());
        assertNull(actual.getTeacherLastName());
        assertNull(actual.getTeacherFirstName());
        assertNull(actual.getSubjects());
        assertNull(actual.getSchool2TeacherList());
        assertNull(actual.getSubject2TeacherList());
    }

    @Test
    void editTeacher() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setTeacherId(1);
        teacherDto.setTeacherFirstName("first");
        teacherDto.setTeacherLastName("last");

        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        teacher.setTeacherFirstName("first");
        teacher.setTeacherLastName("last");

        when(teacherRepository.findById(teacherDto.getTeacherId())).thenReturn(Optional.of(teacher));

        teacherService.editTeacher(teacherDto);

        verify(teacherRepository).save(teacher);
    }

    @Test
    void getTeacherSchools() {
        int teacherId = 1;
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        School2Teacher school2Teacher = new School2Teacher();
        School school = new School();
        school.setSchoolId(10);
        school.setSchoolName("test");
        school2Teacher.setSchool(school);
        List<School2Teacher> school2TeacherList = Collections.singletonList(school2Teacher);

        teacher.setSchool2TeacherList(school2TeacherList);

        School schoolSecond = new School();
        schoolSecond.setSchoolId(10);
        schoolSecond.setSchoolName("test");

        List<School> schoolList = new ArrayList<>();
        schoolList.add(schoolSecond);
        schoolList.add(school);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(schoolRepository.findAll()).thenReturn(schoolList);

        List<School> actual = teacherService.getTeacherSchools(teacherId);

        assertEquals(10, actual.get(0).getSchoolId());
        assertEquals("test", actual.get(0).getSchoolName());
        assertNull(actual.get(0).getSchoolAddress());
        assertNull(actual.get(0).getClassOfStudentsList());
    }

    @Test
    void getSchoolsForTeacherToAdd() {
        int teacherId = 1;
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setTeacher(teacher);
        School school = new School();
        school.setSchoolId(10);
        school.setSchoolName("test");
        school2Teacher.setSchool(school);
        List<School2Teacher> school2TeacherList = Collections.singletonList(school2Teacher);
        teacher.setSchool2TeacherList(school2TeacherList);

        School schoolSecond = new School();
        schoolSecond.setSchoolId(11);
        schoolSecond.setSchoolName("testSecond");
        List<School> schoolList = new ArrayList<>();

        schoolList.add(school);
        schoolList.add(schoolSecond);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(schoolRepository.findAll()).thenReturn(schoolList);

        List<School> actual = teacherService.getSchoolsForTeacherToAdd(teacherId);

        assertEquals(11, actual.get(0).getSchoolId());
        assertEquals("testSecond", actual.get(0).getSchoolName());
        assertNull(actual.get(0).getSchoolAddress());
        assertNull(actual.get(0).getClassOfStudentsList());
    }

    @Test
    void addSchoolToTeacher() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);

        School school = new School();
        school.setSchoolId(2);

        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));
        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));

        teacherService.addSchoolToTeacher(teacher.getTeacherId(), school.getSchoolId());

        verify(school2TeacherRepository).save(argThat(argument ->
                argument.getSchool().getSchoolId() == 2
                        && argument.getTeacher().getTeacherId() == 1));
    }

    @Test
    void deleteSchoolFromTeacher() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);

        School school = new School();
        school.setSchoolId(2);

        List<School2Teacher> school2TeacherList = new ArrayList<>();
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setSchool(school);
        school2Teacher.setTeacher(teacher);
        school2TeacherList.add(school2Teacher);

        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));
        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));
        when(school2TeacherRepository.findAll()).thenReturn(school2TeacherList);

        teacherService.deleteSchoolFromTeacher(teacher.getTeacherId(), school.getSchoolId());

        verify(school2TeacherRepository).delete(school2Teacher);
        verify(teacherRepository).save(teacher);
    }

    @Test
    void getTeacherLastName() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        teacher.setTeacherLastName("test");

        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        String actual = teacherService.getTeacherLastName(teacher.getTeacherId());

        assertEquals("test", actual);
    }

    @Test
    void getTeacherFirstName() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        teacher.setTeacherFirstName("test");

        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        String actual = teacherService.getTeacherFirstName(teacher.getTeacherId());

        assertEquals("test", actual);
    }

    @Test
    void getListOfClassesForTeacher() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);

        Teacher2Class teacher2Class = new Teacher2Class();
        teacher2Class.setTeacher(teacher);
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(10);
        classOfStudents.setSchool(new School());
        teacher2Class.setClassOfStudents(classOfStudents);
        List<Teacher2Class> teacher2ClassList = Collections.singletonList(teacher2Class);

        teacher.setTeacher2ClassList(teacher2ClassList);

        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        List<ClassOfStudentsDto> actual = teacherService.getListOfClassesForTeacher(
                teacher.getTeacherId());

        assertEquals(10, actual.get(0).getClassId());
        assertNull(actual.get(0).getClassName());
        assertNull(actual.get(0).getStudents());
        assertNull(actual.get(0).getClassMaster());
        assertEquals(0, actual.get(0).getSchoolId());
        assertNull(actual.get(0).getClassMasterName());
    }

    @Test
    void getListOfClassesForTeacher_whenTeacherRepositoryDoesNotFindById() {
        int teacherId = 1;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    teacherService.getListOfClassesForTeacher(teacherId);
                }
        );
        assertEquals("Cannot find teacher by id " + teacherId, exception.getMessage());
    }

    @Test
    void getClassListForTeacherToAdd_whenItAddsAClassToList() {
        School school = new School();
        school.setSchoolId(1);

        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);

        Teacher2Class teacher2Class = new Teacher2Class();
        teacher2Class.setId(10);
        teacher2Class.setTeacher(teacher);
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(11);
        teacher2Class.setClassOfStudents(classOfStudents);
        List<Teacher2Class> teacher2ClassList = Collections.singletonList(teacher2Class);

        teacher.setTeacher2ClassList(teacher2ClassList);

        ClassOfStudents classOfStudentsDifferent = new ClassOfStudents();
        classOfStudentsDifferent.setClassId(22);
        List<ClassOfStudents> classOfStudentsDifferentList = new ArrayList<>();
        classOfStudentsDifferentList.add(classOfStudents);
        classOfStudentsDifferentList.add(classOfStudentsDifferent);

        school.setClassOfStudentsList(classOfStudentsDifferentList);

        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));
        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        List<ClassOfStudents> actual = teacherService.getClassListForTeacherToAdd(school.getSchoolId(),
                teacher.getTeacherId());

        assertEquals(22, actual.get(0).getClassId());
    }

    @Test
    void getClassListForTeacherToAdd_whenListReturnsEmpty() {
        School school = new School();
        school.setSchoolId(1);

        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);

        Teacher2Class teacher2Class = new Teacher2Class();
        teacher2Class.setId(10);
        teacher2Class.setTeacher(teacher);
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(11);
        List<ClassOfStudents> classOfStudentsList = Collections.singletonList(classOfStudents);
        teacher2Class.setClassOfStudents(classOfStudents);
        List<Teacher2Class> teacher2ClassList = Collections.singletonList(teacher2Class);

        teacher.setTeacher2ClassList(teacher2ClassList);

        school.setClassOfStudentsList(classOfStudentsList);

        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));
        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        List<ClassOfStudents> actual = teacherService.getClassListForTeacherToAdd(school.getSchoolId(),
                teacher.getTeacherId());

        assertTrue(actual.isEmpty());
    }

    @Test
    void addClassToTeacher() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);

        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(2);

        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));
        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        teacherService.addClassToTeacher(teacher.getTeacherId(), classOfStudents.getClassId());

        verify(teacher2ClassRepository).save(argThat(argument ->
                argument.getTeacher().getTeacherId() == 1 &&
                        argument.getClassOfStudents().getClassId() == 2));
    }

    @Test
    void deleteClassForTeacher() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);

        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(2);
        Teacher2Class teacher2Class = new Teacher2Class();
        teacher2Class.setClassOfStudents(classOfStudents);
        teacher2Class.setTeacher(teacher);
        teacher2Class.setId(10);

        classOfStudents.setTeacher2ClassList(Collections.singletonList(teacher2Class));

        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));
        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        teacherService.deleteClassForTeacher(teacher.getTeacherId(), classOfStudents.getClassId());

        verify(teacher2ClassRepository).delete(teacher2Class);
    }
}