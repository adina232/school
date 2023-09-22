package com.example.school.dto;

import com.example.school.entity.Student;
import com.example.school.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject2StudentDto {
    private int id;
    private float grade;
    private Student student;
    private Subject subject;
}
