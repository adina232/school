package com.example.school.dto;

import com.example.school.entity.School2Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolDto {
    private int schoolId;
    private String schoolName;
    private String schoolAddress;
}
