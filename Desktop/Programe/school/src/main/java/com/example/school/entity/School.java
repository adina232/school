package com.example.school.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SCHOOL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private int schoolId;
    @OneToMany( mappedBy = "school")
    List<School2Teacher> school2TeacherList;
    @Column(name = "school_name")
    private String schoolName;
    @Column(name = "school_address")
    private String SchoolAddress;

    @OneToMany(mappedBy = "school")
    private List<ClassOfStudents> classOfStudentsList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof School)) return false;
        School school = (School) o;
        return schoolId == school.schoolId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schoolId);
    }
}
