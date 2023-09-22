package com.example.school.repository;

import com.example.school.entity.Subject2Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Subject2TeacherRepository extends JpaRepository<Subject2Teacher, Integer> {
}
