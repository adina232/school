package com.example.school.service;

import com.example.school.dto.ClassOfStudentsDto;
import com.example.school.dto.School2TeacherDto;
import com.example.school.dto.StudentDto;
import com.example.school.dto.SubjectDto;
import com.example.school.entity.*;
import com.example.school.repository.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class School2TeacherServiceTest {
    private final SchoolRepository schoolRepository = mock(SchoolRepository.class);
    private final ClassOfStudentsRepository classOfStudentsRepository = mock(ClassOfStudentsRepository.class);
    private final SubjectRepository subjectRepository = mock(SubjectRepository.class);
    private final Subject2ClassRepository subject2ClassRepository = mock(Subject2ClassRepository.class);
    private final School2TeacherRepository school2TeacherRepository = mock(School2TeacherRepository.class);
    private final Subject2StudentRepository subject2StudentRepository = mock(Subject2StudentRepository.class);
    private final StudentRepository studentRepository = mock(StudentRepository.class);
    private final School2TeacherService school2TeacherService = new School2TeacherService(
            schoolRepository, classOfStudentsRepository, subjectRepository, subject2ClassRepository,
            school2TeacherRepository, subject2StudentRepository, studentRepository);

    @Test
    void getList() {
        List<School> schoolList = new ArrayList<>();
        School school = new School();
        school.setSchoolId(1);
        school.setSchoolName("i");

        School schoolSecond = new School();
        schoolSecond.setSchoolId(2);
        schoolSecond.setSchoolName("ii");

        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setId(10);
        school2Teacher.setTeacher(new Teacher());
        school.setSchool2TeacherList(Collections.singletonList(school2Teacher));

        School2Teacher school2TeacherSecond = new School2Teacher();
        school2TeacherSecond.setId(20);
        school2TeacherSecond.setTeacher(new Teacher());
        school2TeacherSecond.setSchool(schoolSecond);
        schoolSecond.setSchool2TeacherList(Collections.singletonList(school2TeacherSecond));

        schoolList.add(schoolSecond);
        schoolList.add(school);

        when(schoolRepository.findAll()).thenReturn(schoolList);

        List<School2TeacherDto> actual = school2TeacherService.getList();

        assertEquals(10, actual.get(0).getId());
        assertEquals(20, actual.get(1).getId());
        assertEquals("i", actual.get(0).getSchoolName());
        assertEquals("ii", actual.get(1).getSchoolName());
        assertNull(actual.get(0).getTeacherFirstName());
        assertNull(actual.get(1).getTeacherFirstName());
        assertNull(actual.get(0).getTeacherLastName());
        assertNull(actual.get(1).getTeacherLastName());
    }

    @Test
    void getSubjectsForSchool2Teacher() {
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setId(1);

        Teacher teacher = new Teacher();
        teacher.setTeacherId(10);

        List<Subject2Teacher> subject2TeacherList = new ArrayList<>();
        Subject2Teacher subject2Teacher = new Subject2Teacher();
        subject2Teacher.setId(100);
        Subject subject = new Subject();
        subject.setSubjectId(11);
        subject.setSubjectName("i");
        subject2Teacher.setSubject(subject);

        Subject2Teacher subject2TeacherSecond = new Subject2Teacher();
        subject2TeacherSecond.setId(200);
        Subject subjectSecond = new Subject();
        subjectSecond.setSubjectId(22);
        subjectSecond.setSubjectName("ii");
        subject2TeacherSecond.setSubject(subjectSecond);

        subject2TeacherList.add(subject2TeacherSecond);
        subject2TeacherList.add(subject2Teacher);

        teacher.setSubject2TeacherList(subject2TeacherList);

        school2Teacher.setTeacher(teacher);

        when(school2TeacherRepository.findById(school2Teacher.getId())).thenReturn(Optional.of(school2Teacher));

        List<SubjectDto> actual = school2TeacherService.getSubjectsForSchool2Teacher(school2Teacher.getId());

        assertEquals(11, actual.get(0).getSubjectId());
        assertEquals(22, actual.get(1).getSubjectId());
        assertEquals("i", actual.get(0).getSubjectName());
        assertEquals("ii", actual.get(1).getSubjectName());
        assertNull(actual.get(0).getTeacher());
        assertNull(actual.get(1).getTeacher());
        assertEquals(0.0f, actual.get(0).getGrade());
        assertEquals(0.0f, actual.get(1).getGrade());
    }

    @Test
    void getClassesForSubjects() {
        Subject subject = new Subject();
        subject.setSubjectId(1);
        subject.setSubjectName("test");

        List<Subject2Class> subject2ClassList = new ArrayList<>();
        Subject2Class subject2Class = new Subject2Class();
        subject2Class.setId(200);
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(23);
        classOfStudents.setClassName("i");
        subject2Class.setClassOfStudents(classOfStudents);
        subject2Class.setSubject(subject);
        subject2ClassList.add(subject2Class);

        subject.setSubject2ClassList(subject2ClassList);

        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setId(10);
        Teacher teacher = new Teacher();
        teacher.setTeacherId(11);
        List<Subject2Teacher> subject2TeacherList = new ArrayList<>();
        Subject2Teacher subject2Teacher = new Subject2Teacher();
        subject2Teacher.setId(100);
        subject2Teacher.setSubject(subject);
        subject2TeacherList.add(subject2Teacher);
        teacher.setSubject2TeacherList(subject2TeacherList);
        school2Teacher.setTeacher(teacher);

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(school2TeacherRepository.findById(school2Teacher.getId()))
                .thenReturn(Optional.of(school2Teacher));

        List<ClassOfStudentsDto> actual = school2TeacherService.getClassesForSubjects(
                school2Teacher.getId(), subject.getSubjectId());

        assertEquals(23, actual.get(0).getClassId());
        assertEquals("i", actual.get(0).getClassName());
    }

    @Test
    void getClassesForSubjects_whenSubjectRepositoryDoesNotFindById() {
        int school2TeacherId = 0;
        int subjectId = 1;
        RuntimeException exception = assertThrows(
                RuntimeException.class, () -> {
                    school2TeacherService.getClassesForSubjects(school2TeacherId, subjectId);
                }
        );
        assertEquals("Cannot find school2Teacher with id " + subjectId, exception.getMessage());
    }

    @Test
    void addClassToSubjectList() {
        School school = new School();
        school.setSchoolId(14);

        School schoolSecond = new School();
        schoolSecond.setSchoolId(15);

        Subject subject = new Subject();
        subject.setSubjectId(1);
        subject.setSubjectName("test");

        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setId(2);
        Teacher teacher = new Teacher();
        teacher.setTeacherId(10);

        Subject2Teacher subject2Teacher = new Subject2Teacher();
        subject2Teacher.setId(16);
        subject2Teacher.setSubject(subject);
        teacher.setSubject2TeacherList(Collections.singletonList(subject2Teacher));

        Subject2Class subject2Class = new Subject2Class();
        subject2Class.setId(17);
        subject2Class.setSubject(subject);
        subject.setSubject2ClassList(Collections.singletonList(subject2Class));

        school2Teacher.setTeacher(teacher);
        school2Teacher.setSchool(school);

        List<ClassOfStudents> classOfStudentsList = new ArrayList<>();

        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(11);
        classOfStudents.setSchool(school);

        ClassOfStudents classOfStudentsSecond = new ClassOfStudents();
        classOfStudentsSecond.setClassId(12);
        classOfStudentsSecond.setSchool(school);

        classOfStudentsList.add(classOfStudents);
        classOfStudentsList.add(classOfStudentsSecond);

        subject2Class.setClassOfStudents(classOfStudents);

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(school2TeacherRepository.findById(school2Teacher.getId()))
                .thenReturn(Optional.of(school2Teacher));
        when(classOfStudentsRepository.findAll()).thenReturn(classOfStudentsList);

        List<ClassOfStudentsDto> actual = school2TeacherService.addClassToSubjectList(school2Teacher
                .getId(), subject.getSubjectId());

        assertEquals(12, actual.get(0).getClassId());
        assertNull(actual.get(0).getClassName());
        assertNull(actual.get(0).getStudents());
        assertNull(actual.get(0).getClassMaster());
        assertNull(actual.get(0).getClassMasterName());
        assertNull(actual.get(0).getSchoolId());
    }

    @Test
    void deleteClassForSubject() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);

        Subject subject = new Subject();
        subject.setSubjectId(2);

        Subject2Class subject2Class = new Subject2Class();
        subject2Class.setSubject(subject);
        subject2Class.setClassOfStudents(classOfStudents);

        classOfStudents.setSubject2ClassList(Collections.singletonList(subject2Class));

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        school2TeacherService.deleteClassForSubject(classOfStudents.getClassId(), subject.getSubjectId());

        verify(subject2ClassRepository).delete(subject2Class);
    }

    @Test
    void addClassToSubject() {
        Subject subject = new Subject();
        subject.setSubjectId(1);

        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(2);

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        school2TeacherService.addClassToSubject(subject.getSubjectId(), classOfStudents.getClassId());

        verify(subject2ClassRepository).save(argThat(argument ->
                argument.getSubject().getSubjectId() == 1
                        && argument.getClassOfStudents().getClassId() == 2));
    }

    @Test
    void getStudentListForCatalog() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);

        Subject subject = new Subject();
        subject.setSubjectId(2);
        Subject2Class subject2Class = new Subject2Class();
        Student student = new Student();
        student.setStudentId(10);
        Subject2Student subject2Student = new Subject2Student();
        subject2Student.setId(3);
        subject2Student.setSubject(subject);
        subject2Student.setGrade(5f);

        classOfStudents.setStudents(Collections.singletonList(student));
        student.setSubject2StudentList(Collections.singletonList(subject2Student));
        subject2Class.setClassOfStudents(classOfStudents);
        subject.setSubject2ClassList(Collections.singletonList(subject2Class));

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        List<StudentDto> actual = school2TeacherService.getStudentListForCatalog(subject
                .getSubjectId(), classOfStudents.getClassId());

        assertEquals(10, actual.get(0).getStudentId());
        assertEquals(5f, actual.get(0).getGrade());
    }

    @Test
    void addGrade() {
        Subject subject = new Subject();
        subject.setSubjectId(1);

        Student student = new Student();
        student.setStudentId(2);

        float grade = 5f;

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(studentRepository.findById(student.getStudentId())).thenReturn(Optional.of(student));

        school2TeacherService.addGrade(subject.getSubjectId(), student.getStudentId(),
                grade);

        verify(subject2StudentRepository).save(argThat(argument ->
                argument.getStudent().getStudentId() == 2 &&
                        argument.getSubject().getSubjectId() == 1 &&
                        argument.getGrade() == grade));
    }

    @Test
    void getGrade() {
        Subject subject = new Subject();
        subject.setSubjectId(1);
        Subject2Student subject2Student = new Subject2Student();

        Student student = new Student();
        student.setStudentId(2);

        subject2Student.setStudent(student);
        subject2Student.setGrade(5f);
        subject.setSubject2StudentList(Collections.singletonList(subject2Student));

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(studentRepository.findById(student.getStudentId())).thenReturn(Optional.of(student));

        float actual = school2TeacherService.getGrade(subject.getSubjectId(), student.getStudentId());

        assertEquals(5f, actual);
    }

    @Test
    void getSubjectName() {
        Subject subject = new Subject();
        subject.setSubjectId(1);
        subject.setSubjectName("test");

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));

        String actual = school2TeacherService.getSubjectName(subject.getSubjectId());

        assertEquals("test", actual);
    }

    @Test
    void getTeacherName() {
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setId(1);
        Teacher teacher = new Teacher();
        teacher.setTeacherFirstName("first");
        teacher.setTeacherLastName("last");
        school2Teacher.setTeacher(teacher);

        when(school2TeacherRepository.findById(school2Teacher.getId()))
                .thenReturn(Optional.of(school2Teacher));

        String actual = school2TeacherService.getTeacherName(school2Teacher.getId());

        assertEquals("first last", actual);
    }

    @Test
    void getSchoolName() {
        School2Teacher school2Teacher = new School2Teacher();
        school2Teacher.setId(1);
        School school = new School();
        school.setSchoolName("test");
        school2Teacher.setSchool(school);

        when(school2TeacherRepository.findById(school2Teacher.getId()))
                .thenReturn(Optional.of(school2Teacher));

        String actual = school2TeacherService.getSchoolName(school2Teacher.getId());

        assertEquals("test", actual);
    }

    @Test
    void getClassName() {
        ClassOfStudents classOfStudents = new ClassOfStudents();
        classOfStudents.setClassId(1);
        classOfStudents.setClassName("test");

        when(classOfStudentsRepository.findById(classOfStudents.getClassId()))
                .thenReturn(Optional.of(classOfStudents));

        String actual = school2TeacherService.getClassName(classOfStudents.getClassId());

        assertEquals("test", actual);
    }
}