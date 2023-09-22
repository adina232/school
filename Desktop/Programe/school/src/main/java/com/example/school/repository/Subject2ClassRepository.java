package com.example.school.repository;

import com.example.school.entity.Subject2Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Subject2ClassRepository extends JpaRepository<Subject2Class, Integer> {
}
