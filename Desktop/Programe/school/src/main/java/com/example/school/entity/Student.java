package com.example.school.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "STUDENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentId;

    @Column(name = "student_first_name")
    private String studentFirstName;
    @Column(name = "student_last_name")
    private String studentLastName;
    @Column(name = "student_date_of_birth")
    private Date studentDateOfBirth;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassOfStudents classOfStudents;

    @OneToMany(mappedBy = "student")
    private List<Subject2Student> subject2StudentList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return studentId == student.studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
