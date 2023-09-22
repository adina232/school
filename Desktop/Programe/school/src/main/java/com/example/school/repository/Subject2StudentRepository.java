package com.example.school.repository;

import com.example.school.entity.Subject2Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Subject2StudentRepository extends JpaRepository<Subject2Student, Integer> {
}
