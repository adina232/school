package com.example.school.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CLASS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClassOfStudents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classOfStudents_id")
    private int classId;

    @Column(name = "classOfStudents_name")
    private String className;

    @OneToMany(mappedBy = "classOfStudents")
    private List<Student> students;

    @OneToOne
    @JoinColumn(name = "CLASS_MASTER_ID")
    private Teacher classMaster;

    @JoinColumn(name = "SCHOOL_ID")
    @ManyToOne
    private School school;

    @OneToMany(mappedBy = "classOfStudents")
    private List<Teacher2Class> teacher2ClassList;

    @OneToMany(mappedBy = "classOfStudents")
    private List<Subject2Class> subject2ClassList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassOfStudents)) return false;
        ClassOfStudents that = (ClassOfStudents) o;
        return classId == that.classId && Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, className);
    }
}
