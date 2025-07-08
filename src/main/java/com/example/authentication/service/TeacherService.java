package com.example.authentication.service;

import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.model.Teacher;
import com.example.authentication.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(TeacherRegisterRequest request) {
        if (teacherRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Teacher teacher = new Teacher();
        teacher.setUsername(request.getUsername());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setEmail(request.getEmail());
        // teacher.setSubject(request.getSubject());

        teacherRepository.save(teacher);
        return "Teacher registration successful for: " + request.getUsername();
    }

    public String login(TeacherRegisterRequest request) {
        Teacher teacher = teacherRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (!passwordEncoder.matches(request.getPassword(), teacher.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return "Login successful for teacher: " + teacher.getUsername();
    }
}
