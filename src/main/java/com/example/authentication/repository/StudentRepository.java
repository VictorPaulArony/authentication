package com.example.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.authentication.model.Student;

import java.util.Optional;
// This interface extends JpaRepository for the CRUD operations 
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUsername(String username);
    
}
