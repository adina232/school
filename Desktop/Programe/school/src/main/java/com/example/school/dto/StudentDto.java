package com.example.school.dto;

import com.example.school.entity.ClassOfStudents;
import com.example.school.entity.Subject2Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Integer studentId;
    private String studentFirstName;
    private String studentLastName;
    private String studentDateOfBirth;
    private List<Subject2Student> subject2StudentList;
    private ClassOfStudents classOfStudents;

    private float grade;
}
