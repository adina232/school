package com.example.school.service;

import com.example.school.dto.SubjectDto;
import com.example.school.entity.Subject;
import com.example.school.entity.Subject2Teacher;
import com.example.school.entity.Teacher;
import com.example.school.repository.Subject2TeacherRepository;
import com.example.school.repository.SubjectRepository;
import com.example.school.repository.TeacherRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class SubjectServiceTest {
    private final SubjectRepository subjectRepository = mock(SubjectRepository.class);
    private final TeacherRepository teacherRepository = mock(TeacherRepository.class);
    private final Subject2TeacherRepository subject2TeacherRepository =
            mock(Subject2TeacherRepository.class);
    private final SubjectService subjectService = new SubjectService(subjectRepository,
            teacherRepository, subject2TeacherRepository);

    @Test
    void addSubject() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setSubjectId(1);
        subjectDto.setSubjectName("test");

        Subject subject = new Subject();
        subject.setSubjectId(1);
        subject.setSubjectName("test");

        subjectService.addSubject(subjectDto);

        verify(subjectRepository).save(subject);
    }

    @Test
    void getSubjects() {
        Subject subject = new Subject();
        subject.setSubjectId(1);
        subject.setSubjectName("test");
        List<Subject> subjectList = Collections.singletonList(subject);
        when(subjectRepository.findAll()).thenReturn(subjectList);

        List<SubjectDto> actual = subjectService.getSubjects();

        assertEquals(1, actual.get(0).getSubjectId());
        assertEquals("test", actual.get(0).getSubjectName());
        assertNull(actual.get(0).getTeacher());
        assertEquals(0f, actual.get(0).getGrade());
    }

    @Test
    void getTeachersForSubject() {
        Subject subject = new Subject();
        Subject2Teacher subject2Teacher = new Subject2Teacher();
        subject2Teacher.setId(10);
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1);
        teacher.setTeacherLastName("test");
        subject2Teacher.setTeacher(teacher);
        subject.setSubject2TeacherList(Collections.singletonList(subject2Teacher));
        subject2Teacher.setSubject(subject);

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));

        List<Teacher> actual = subjectService.getTeachersForSubject(subject.getSubjectId());

        assertEquals(1, actual.get(0).getTeacherId());
        assertEquals("test", actual.get(0).getTeacherLastName());
        assertNull(actual.get(0).getTeacherFirstName());
        assertNull(actual.get(0).getTeacher2ClassList());
        assertNull(actual.get(0).getSchool2TeacherList());
        assertNull(actual.get(0).getSubject2TeacherList());
        assertNull(actual.get(0).getClassOfStudents());
        assertNull(actual.get(0).getTeacher2ClassList());
    }

    @Test
    void getTeachersToAdd() {
        Subject subject = new Subject();
        subject.setSubjectId(1);
        Subject2Teacher subject2Teacher = new Subject2Teacher();
        Teacher teacher = new Teacher();
        teacher.setTeacherId(10);
        subject2Teacher.setTeacher(teacher);
        List<Subject2Teacher> subject2TeacherList = Collections.singletonList(subject2Teacher);

        subject.setSubject2TeacherList(subject2TeacherList);

        List<Teacher> teachers = new ArrayList<>();
        Teacher teacherSecond = new Teacher();
        teacherSecond.setTeacherId(20);

        teachers.add(teacherSecond);
        teachers.add(teacher);

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> actual = subjectService.getTeachersToAdd(subject.getSubjectId());

        assertEquals(20, actual.get(0).getTeacherId());
        assertNull(actual.get(0).getTeacherLastName());
        assertNull(actual.get(0).getTeacherFirstName());
        assertNull(actual.get(0).getTeacher2ClassList());
        assertNull(actual.get(0).getSchool2TeacherList());
        assertNull(actual.get(0).getSubject2TeacherList());
        assertNull(actual.get(0).getClassOfStudents());
        assertNull(actual.get(0).getTeacher2ClassList());
    }

    @Test
    void addTeacherToSubject() {
        Subject subject = new Subject();
        subject.setSubjectId(1);

        Teacher teacher = new Teacher();
        teacher.setTeacherId(9);

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));
        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        subjectService.addTeacherToSubject(subject.getSubjectId(), teacher.getTeacherId());

        verify(subject2TeacherRepository).save(argThat(argument ->
                argument.getSubject().getSubjectId() == 1 &&
                        argument.getTeacher().getTeacherId() == 9));
    }

    @Test
    void deleteSubject() {
        int subjectId = 1;
        subjectService.deleteSubject(subjectId);
        verify(subjectRepository).deleteById(subjectId);
    }

    @Test
    void deleteTeacherFromSubject() {
        int teacherId = 2;
        Subject subject = new Subject();
        subject.setSubjectId(1);
        Subject2Teacher subject2Teacher = new Subject2Teacher();
        Teacher teacher = new Teacher();
        teacher.setTeacherId(2);
        subject2Teacher.setTeacher(teacher);
        List<Subject2Teacher> subject2TeacherList = Collections.singletonList(subject2Teacher);
        subject.setSubject2TeacherList(subject2TeacherList);

        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));

        subjectService.deleteTeacherFromSubject(subject.getSubjectId(), teacherId);

        verify(subject2TeacherRepository).delete(subject2Teacher);
    }

    @Test
    void getSubjectName() {
        Subject subject = new Subject();
        subject.setSubjectId(1);
        subject.setSubjectName("test");
        when(subjectRepository.findById(subject.getSubjectId())).thenReturn(Optional.of(subject));

        String actual = subjectService.getSubjectName(subject.getSubjectId());

        assertEquals("test", actual);
    }
}