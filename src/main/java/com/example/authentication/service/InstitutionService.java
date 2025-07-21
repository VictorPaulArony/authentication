package com.example.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.authentication.dto.InstitutionLoginDTO;
import com.example.authentication.dto.InstitutionRegistrationDTO;
import com.example.authentication.model.Institution;
import com.example.authentication.repository.InstitutionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InstitutionService implements UserDetailsService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Transactional
    public Institution register(InstitutionRegistrationDTO dto) {
        if (institutionRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (institutionRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone number already in use");
        }

        Institution institution = Institution.builder()
                .schoolnamne(dto.getSchoolnamne())
                .registraitionNumber(dto.getRegistraitionNumber())
                .schoolType(Institution.SchoolType.valueOf(dto.getSchoolType().toUpperCase()))
                .educationSystem(Institution.EducationSystem.valueOf(dto.getEducationSystem().toUpperCase().replace("-", "_")))
                .location(dto.getLocation())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        return institutionRepository.save(institution);
    }

    public Institution login(InstitutionLoginDTO dto) {
        Institution institution = institutionRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), institution.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return institution;
    }

    public Institution findByEmail(String email) {
        return institutionRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Institution institution = institutionRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Institution not found"));

        return User.builder()
                .username(institution.getEmail())
                .password(institution.getPassword())
                .authorities("ROLE_INSTITUTION")
                .build();
    }
}
