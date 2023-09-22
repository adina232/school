package com.example.school.service;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.SchoolDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.TeacherDto;
import com.example.school.entity.*;
import com.example.school.repository.*;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchoolServiceTest {
    private final SchoolRepository schoolRepository = mock(SchoolRepository.class);
    private final TeacherRepository teacherRepository = mock(TeacherRepository.class);
    private final School2TeacherRepository school2TeacherRepository = mock(School2TeacherRepository.class);
    private final ClassOfStudentsRepository classOfStudentsRepository = mock(ClassOfStudentsRepository.class);
    private final StudentRepository studentRepository = mock(StudentRepository.class);

    private final SchoolService schoolService = new SchoolService(schoolRepository,
            teacherRepository, school2TeacherRepository, classOfStudentsRepository, studentRepository);

    @Test
    void addSchool() {
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolId(1);
        schoolDto.setSchoolName("school");
        schoolDto.setSchoolAddress("test");

        School school = new School();
        school.setSchoolId(1);
        school.setSchoolName("school");
        school.setSchoolAddress("test");

        schoolService.addSchool(schoolDto);

        verify(schoolRepository).save(school);
    }

    @Test
    void getSchools() {
        List<School> schoolList = new ArrayList<>();
        School school = new School();
        school.setSchoolId(1);
        schoolList.add(school);
        when(schoolRepository.findAll()).thenReturn(schoolList);

        List<SchoolDto> actual = schoolService.getSchools();

        assertEquals(1, actual.get(0).getSchoolId());
    }

    @Test
    void deleteSchool() {
        int schoolId = 1;
        schoolService.deleteSchool(schoolId);
        verify(schoolRepository).deleteById(schoolId);
    }

    @Test
    void getSchool() {
        School school = new School();
        school.setSchoolId(1);
        school.setSchoolName("school");
        when(schoolRepository.findById(1)).thenReturn(Optional.of(school));

        SchoolDto actual = schoolService.getSchool(school.getSchoolId());

        assertEquals(1, actual.getSchoolId());
        assertEquals("school", actual.getSchoolName());
        assertNull(actual.getSchoolAddress());
    }

    @Test
    void editSchool() {
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setSchoolId(1);
        schoolDto.setSchoolName("school");

        School school = new School();
        school.setSchoolId(1);
        school.setSchoolName("school");

        when(schoolRepository.findById(1)).thenReturn(Optional.of(school));

        schoolService.editSchool(schoolDto);

        verify(schoolRepository).save(school);
    }

    @Test
    void getTeachersInSchool() {
        int schoolId = 1;
        School school = new School();
        school.setSchoolId(1);

        List<School2Teacher> school2TeacherList = new ArrayList<>();
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setSchool(school);
        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);
        teacher.setTeacherLastName("test");
        school2Teacher.setTeacher(teacher);
        school2TeacherList.add(school2Teacher);

        school.setSchool2TeacherList(school2TeacherList);

        when(schoolRepository.findById(1)).thenReturn(Optional.of(school));

        List<TeacherDto> actual = schoolService.getTeachersInSchool(schoolId);

        assertEquals(2, actual.get(0).getTeacherId());
        assertEquals("test", actual.get(0).getTeacherLastName());
        assertNull(actual.get(0).getTeacherFirstName());
    }

    @Test
    void deleteTeacherFromSeeTeachers() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        School school = new School();
        school.setSchoolId(2);

        List<School2Teacher> school2TeacherList = new ArrayList<>();
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setId(3);
        school2Teacher.setSchool(school);
        school2Teacher.setTeacher(teacher);

        school2TeacherList.add(school2Teacher);

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(schoolRepository.findById(2)).thenReturn(Optional.of(school));
        when(school2TeacherRepository.findAll()).thenReturn(school2TeacherList);

        schoolService.deleteTeacherFromSeeTeachers(1, 2);

        verify(school2TeacherRepository).delete(school2Teacher);
    }

    @Test
    void getTeachersForSchool_whenListIsEmpty() {
        School school = new School();
        school.setSchoolId(1);
        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);

        List<School2Teacher> school2TeacherList = new ArrayList<>();
        School2Teacher school2Teacher = new School2Teacher(3, school, teacher);
        school2TeacherList.add(school2Teacher);
        school.setSchool2TeacherList(school2TeacherList);
        teacher.setSchool2TeacherList(school2TeacherList);

        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        when(schoolRepository.findById(1)).thenReturn(Optional.of(school));
        when(teacherRepository.findAll()).thenReturn(teacherList);

        List<Teacher> actual = schoolService.getTeachersForSchool(1);

        assertTrue(actual.isEmpty());
    }

    @Test
    void getTeachersForSchool_whenListIsNotEmpty() {
        School school = new School();
        school.setSchoolId(1);
        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);

        List<School2Teacher> school2TeacherList = new ArrayList<>();
        School2Teacher school2Teacher = new School2Teacher(3, school, teacher);
        school2TeacherList.add(school2Teacher);
        school.setSchool2TeacherList(school2TeacherList);
        teacher.setSchool2TeacherList(school2TeacherList);

        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacherNotOnTheList = new Teacher();
        teacherList.add(teacher);
        teacherList.add(teacherNotOnTheList);

        when(schoolRepository.findById(1)).thenReturn(Optional.of(school));
        when(teacherRepository.findAll()).thenReturn(teacherList);

        List<Teacher> actual = schoolService.getTeachersForSchool(1);

        assertEquals(teacherNotOnTheList, actual.get(0));
    }

    @Test
    void addTeacherToSchool() {
        School school = new School();
        school.setSchoolId(1);
        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setSchool(school);
        school2Teacher.setTeacher(teacher);

        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));
        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        schoolService.addTeacherToSchool(school.getSchoolId(), teacher.getTeacherId());

        verify(school2TeacherRepository).save(school2Teacher);
    }

    @Test
    void getClassListForSchool_whenSchoolIsFoundBySchoolId() {
        School school = new School();
        school.setSchoolId(1);
        List<ClassOfStudents> classOfStudentsList = new ArrayList<>();
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(2);
        classOfStudents.setClassName("first");
        classOfStudents.setClassMaster(new Teacher());

        ClassOfStudents classOfStudentsSecond = new ClassOfStudents();
        classOfStudentsSecond.setClassId(3);
        classOfStudentsSecond.setClassName("second");
        classOfStudentsSecond.setClassMaster(new Teacher());

        classOfStudentsList.add(classOfStudentsSecond);
        classOfStudentsList.add(classOfStudents);
        school.setClassOfStudentsList(classOfStudentsList);

        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));

        List<ClassOfStudentsDto> actual = schoolService.getClassListForSchool(school.getSchoolId());

        assertEquals(2, actual.get(0).getClassId());
        assertEquals(3, actual.get(1).getClassId());
        assertEquals("first", actual.get(0).getClassName());
        assertEquals("second", actual.get(1).getClassName());
    }

    @Test
    void getClassListForSchool_whenSchoolIsNotFoundBySchoolId() {
        int schoolId = 1;
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> {
                    schoolService.getClassListForSchool(schoolId);
                }
        );
        assertEquals("Cannot find school with id " + schoolId, exception.getMessage());
    }

    @Test
    void addClassOfStudents() {
        ClassOfStudentsDto classOfStudentsDto = new ClassOfStudentsDto();
        classOfStudentsDto.setClassId(1);
        classOfStudentsDto.setSchoolId(2);
        classOfStudentsDto.setClassMaster(3);

        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        School school = new School();
        school.setSchoolId(2);
        classOfStudents.setSchool(school);
        classOfStudents.setClassMaster(new Teacher());

        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));
        when(teacherRepository.findById(3)).thenReturn(Optional.of(new Teacher()));

        schoolService.addClassOfStudents(classOfStudentsDto);

        verify(classOfStudentsRepository).save(classOfStudents);
    }

    @Test
    void addClassOfStudents_whenSchoolRepositoryThrowsException() {
        ClassOfStudentsDto classOfStudentsDto = new ClassOfStudentsDto();
        classOfStudentsDto.setClassId(1);
        classOfStudentsDto.setSchoolId(2);
        classOfStudentsDto.setClassMaster(3);

        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    schoolService.addClassOfStudents(classOfStudentsDto);
                }
        );
        assertEquals("Cannot find class with id " + classOfStudentsDto.getSchoolId(),
                exception.getMessage());
    }

    @Test
    void addClassOfStudents_whenTeacherRepositoryThrowsException() {
        ClassOfStudentsDto classOfStudentsDto = new ClassOfStudentsDto();
        classOfStudentsDto.setClassId(1);
        classOfStudentsDto.setSchoolId(2);
        classOfStudentsDto.setClassMaster(3);

        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        School school = new School();
        school.setSchoolId(2);
        classOfStudents.setSchool(school);
        classOfStudents.setClassMaster(new Teacher());

        when(schoolRepository.findById(school.getSchoolId())).thenReturn(Optional.of(school));

        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    schoolService.addClassOfStudents(classOfStudentsDto);
                }
        );
        assertEquals("Can not find teacher with id" +
                classOfStudentsDto.getClassMaster(), exception.getMessage());
    }

    @Test
    void getTeachersOptions() {
        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        teacherList.add(teacher);

        when(teacherRepository.findAll()).thenReturn(teacherList);

        List<TeacherDto> actual = schoolService.getTeachersOptions();

        assertEquals(1, actual.get(0).getTeacherId());
        assertNull(actual.get(0).getTeacherFirstName());
        assertNull(actual.get(0).getTeacherLastName());
        assertNull(actual.get(0).getSubject2TeacherList());
        assertNull(actual.get(0).getSchool2TeacherList());
    }

    @Test
    void getSchoolId() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        School school = new School();
        school.setSchoolId(2);
        classOfStudents.setSchool(school);

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        int actual = schoolService.getSchoolId(classOfStudents.getClassId());

        assertEquals(2, actual);
    }

    @Test
    void deleteClassForSchool() {
        schoolService.deleteClassForSchool(1);

        verify(classOfStudentsRepository).deleteById(1);
    }

    @Test
    void getStudentsInClass() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);

        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setStudentId(2);
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        student.setStudentDateOfBirth(date);
        studentList.add(student);
        classOfStudents.setStudents(studentList);

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        List<StudentDto> actual = schoolService.getStudentsInClass(classOfStudents.getClassId());

        assertEquals(2, actual.get(0).getStudentId());
        assertEquals(String.valueOf(date), actual.get(0).getStudentDateOfBirth());
        assertNull(actual.get(0).getStudentFirstName());
        assertNull(actual.get(0).getStudentLastName());
    }

    @Test
    void getStudentsInClass_whenClassOfStudentsIsNotFoundByClassId() {
        int classId = 1;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    schoolService.getStudentsInClass(classId);
                }
        );
        assertEquals("Cannot find school by id " + classId, exception.getMessage());
    }


    @Test
    void getClassName() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        classOfStudents.setClassName("test");

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        String actual = schoolService.getClassName(classOfStudents.getClassId());

        assertEquals("test", actual);
    }

    @Test
    void deleteStudentForClass() {
        Student student = new Student();
        student.setStudentId(1);
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(2);
        student.setClassOfStudents(classOfStudents);

        when(studentRepository.findById(student.getStudentId())).thenReturn(Optional.of(student));

        schoolService.deleteStudentForClass(student.getStudentId());

        verify(studentRepository).save(student);
    }

    @Test
    void deleteStudentForClass_studentRepositoryException() {
        int studentId = 1;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    schoolService.deleteStudentForClass(studentId);
                }
        );
        assertEquals("Cannot find school by id " + studentId, exception.getMessage());
    }


    @Test
    void getListOfStudentsToAddToClass() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        student.setStudentId(2);
        student.setStudentLastName("i");
        student.setStudentDateOfBirth(date);
        Student studentSecond = new Student();
        studentSecond.setStudentId(3);
        studentSecond.setStudentLastName("ii");
        studentSecond.setStudentDateOfBirth(date);

        studentList.add(studentSecond);
        studentList.add(student);

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));
        when(studentRepository.findAll()).thenReturn(studentList);

        List<StudentDto> actual = schoolService.getListOfStudentsToAddToClass(classOfStudents.getClassId());

        assertEquals(2, actual.get(0).getStudentId());
        assertEquals(3, actual.get(1).getStudentId());
        assertEquals("i", actual.get(0).getStudentLastName());
        assertEquals("ii", actual.get(1).getStudentLastName());
        assertNull(actual.get(0).getStudentFirstName());
        assertNull(actual.get(1).getStudentFirstName());
        assertEquals(String.valueOf(date), actual.get(0).getStudentDateOfBirth());
        assertEquals(String.valueOf(date), actual.get(1).getStudentDateOfBirth());
    }

    @Test
    void getListOfStudentsToAddToClass_whenClassOfStudentsRepositoryThrowsException() {
        int classId = 1;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    schoolService.getListOfStudentsToAddToClass(classId);
                }
        );
        assertEquals("Cannot find school by id " + classId, exception.getMessage());
    }

    @Test
    void addStudentToClass() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        Student student = new Student();
        student.setStudentId(2);

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));
        when(studentRepository.findById(student.getStudentId()))
                .thenReturn(Optional.of(student));

        schoolService.addStudentToClass(classOfStudents.getClassId(), student.getStudentId());

        verify(studentRepository).save(student);
    }

    @Test
    void addStudentToClass_whenClassOfStudentsRepositoryThrowsException() {
        int classId = 1;
        int studentId = 2;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    schoolService.addStudentToClass(classId, studentId);
                }
        );
        assertEquals("Cannot find class by id " + classId, exception.getMessage());
    }

    @Test
    void addStudentToClass_whenStudentRepositoryThrowsException() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        int studentId = 2;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    schoolService.addStudentToClass(classOfStudents.getClassId(), studentId);
                }
        );
        assertEquals("Cannot find student by id " + studentId, exception.getMessage());
    }

}