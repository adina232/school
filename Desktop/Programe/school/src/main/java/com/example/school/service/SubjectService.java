package com.example.school.service;

import com.example.school.dto.SubjectDto;
import com.example.school.entity.Subject;
import com.example.school.entity.Subject2Teacher;
import com.example.school.entity.Teacher;
import com.example.school.repository.Subject2TeacherRepository;
import com.example.school.repository.SubjectRepository;
import com.example.school.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final Subject2TeacherRepository subject2TeacherRepository;

    public void addSubject(SubjectDto subjectDto) {
        subjectRepository.save(setSubject(subjectDto));
    }

    public List<SubjectDto> getSubjects() {
        List<Subject> subjectList = subjectRepository.findAll();
        List<SubjectDto> subjectDtoList = new ArrayList<>();
        for (Subject subject : subjectList) {
            subjectDtoList.add(setSubjectDto(subject));
        }
        return subjectDtoList;
    }

    public List<Teacher> getTeachersForSubject(int subjectId) {
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        List<Subject2Teacher> subject2TeacherList = subject.get().getSubject2TeacherList();
        List<Teacher> teacherList = new ArrayList<>();
        for (Subject2Teacher subject2Teacher : subject2TeacherList) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(subject2Teacher.getTeacher().getTeacherId());
            teacher.setTeacherFirstName(subject2Teacher.getTeacher().getTeacherFirstName());
            teacher.setTeacherLastName(subject2Teacher.getTeacher().getTeacherLastName());
            teacher.setSchool2TeacherList(subject2Teacher.getTeacher().getSchool2TeacherList());
            teacherList.add(teacher);
        }
        return teacherList;
    }

    public List<Teacher> getTeachersToAdd(int subjectId) {
        List<Teacher> teacherList = new ArrayList<>();
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        List<Subject2Teacher> subject2TeacherList = subject.get().getSubject2TeacherList();
        List<Teacher> teachers = new ArrayList<>();
        for (Subject2Teacher subject2Teacher : subject2TeacherList) {
            teachers.add(subject2Teacher.getTeacher());
        }
        List<Teacher> allTeachers = teacherRepository.findAll();
        for (Teacher teacher : allTeachers) {
            if (!teachers.contains(teacher)) {
                teacherList.add(teacher);
            }
        }
        return teacherList;
    }

    public void addTeacherToSubject(int subjectId, int teacherId) {
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Subject2Teacher subject2Teacher = new Subject2Teacher();
        subject2Teacher.setTeacher(teacher.get());
        subject2Teacher.setSubject(subject.get());
        subject2TeacherRepository.save(subject2Teacher);
    }

    public void deleteSubject(int subjectId) {
        subjectRepository.deleteById(subjectId);
    }

    public void deleteTeacherFromSubject(int subjectId, int teacherId) {
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        List<Subject2Teacher> subject2TeacherList = subject.get().getSubject2TeacherList();
        for (Subject2Teacher subject2Teacher : subject2TeacherList) {
            if (subject2Teacher.getTeacher().getTeacherId() == teacherId) {
                subject2TeacherRepository.delete(subject2Teacher);
            }
        }
    }

    public String getSubjectName(int subjectId) {
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        return subject.get().getSubjectName();
    }


    private Subject setSubject(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setSubjectName(subjectDto.getSubjectName());
        subject.setSubjectId(subjectDto.getSubjectId());
        return subject;
    }

    private SubjectDto setSubjectDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setSubjectId(subject.getSubjectId());
        subjectDto.setSubjectName(subject.getSubjectName());
        return subjectDto;
    }
}
