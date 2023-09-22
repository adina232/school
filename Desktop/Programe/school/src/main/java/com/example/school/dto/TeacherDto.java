package com.example.school.dto;

import com.example.school.entity.School2Teacher;
import com.example.school.entity.Subject;
import com.example.school.entity.Subject2Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {
    private int teacherId;
    private String teacherFirstName;
    private String teacherLastName;
    private List<Subject> subjects;
    private List<School2Teacher> school2TeacherList;
    private List<Subject2Teacher> subject2TeacherList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherDto)) return false;
        TeacherDto that = (TeacherDto) o;
        return teacherId == that.teacherId && Objects.equals(teacherFirstName, that.teacherFirstName) && Objects.equals(teacherLastName, that.teacherLastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, teacherFirstName, teacherLastName);
    }
}
