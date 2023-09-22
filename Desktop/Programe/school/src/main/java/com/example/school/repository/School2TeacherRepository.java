package com.example.school.repository;

import com.example.school.entity.School2Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface School2TeacherRepository extends JpaRepository<School2Teacher, Integer> {
}
