package com.example.authentication.repository;

import com.example.authentication.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    Optional<Institution> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
