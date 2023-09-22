package com.example.school.dto;

import com.example.school.entity.Subject2Class;
import com.example.school.entity.Subject2Student;
import com.example.school.entity.Subject2Teacher;
import com.example.school.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
    private int subjectId;
    private String subjectName;
    private float grade;
    private String teacher;

}
