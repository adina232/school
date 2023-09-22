package com.example.school.repository;

import com.example.school.entity.Teacher2Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Teacher2ClassRepository extends JpaRepository<Teacher2Class, Integer> {
}
