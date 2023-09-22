package com.example.school.dto;

import com.example.school.entity.Student;
import com.example.school.entity.Subject2Class;
import com.example.school.entity.Teacher;
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
public class ClassOfStudentsDto {
    private int classId;
    private String className;
    private List<Student> students;
    private Integer classMaster;
    private String classMasterName;
    private Integer schoolId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassOfStudentsDto)) return false;
        ClassOfStudentsDto that = (ClassOfStudentsDto) o;
        return classId == that.classId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId);
    }
}
