package com.example.school.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TEACHER")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private int teacherId;
    @OneToMany(
            mappedBy = "teacher"
    )
    private List<School2Teacher> school2TeacherList;
    @Column(name = "teacher_first_name")
    private String teacherFirstName;
    @Column(name = "teacher_last_name")
    private String teacherLastName;
    @OneToMany(mappedBy = "teacher")
    private List<Subject2Teacher> subject2TeacherList;
    @OneToOne(mappedBy = "classMaster")
    private ClassOfStudents classOfStudents;
    @OneToMany(mappedBy = "teacher")
    private List<Teacher2Class> teacher2ClassList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return teacherId == teacher.teacherId && Objects.equals(teacherFirstName, teacher.teacherFirstName)
                && Objects.equals(teacherLastName, teacher.teacherLastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, teacherFirstName, teacherLastName);
    }
}
