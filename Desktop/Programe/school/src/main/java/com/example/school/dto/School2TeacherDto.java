package com.example.school.dto;

import com.example.school.entity.School;
import com.example.school.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class School2TeacherDto {
    private int id;
    private String schoolName;
    private String teacherFirstName;
    private String teacherLastName;
}
