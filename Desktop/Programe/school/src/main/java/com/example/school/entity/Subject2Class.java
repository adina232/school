package com.example.school.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SUBJECT_TO_CLASS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject2Class {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassOfStudents classOfStudents;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject2Class)) return false;
        Subject2Class that = (Subject2Class) o;
        return id == that.id && Objects.equals(subject, that.subject) && Objects.equals(classOfStudents, that.classOfStudents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, classOfStudents);
    }
}
