package com.example.school.repository;

import com.example.school.entity.ClassOfStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassOfStudentsRepository extends JpaRepository<ClassOfStudents, Integer> {
}
