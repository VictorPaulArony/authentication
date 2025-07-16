package com.example.authentication.service;

import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.model.Student;
import com.example.authentication.repository.StudentRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class StudentService implements UserDetailsService {
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Method to register a new student
    public String register(RegisterRequest request) {
        if (studentRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setFirstname(request.getFirstname());
        student.setLastname(request.getLastname());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        studentRepository.save(student);
        return "Registration successful for user: " + request.getUsername();
    }
    
    // Method to find a student by username
    public String login(RegisterRequest request) {
        Optional<Student> studentOptional = studentRepository.findByUsername(request.getUsername());
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Student student = studentOptional.get();
        if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return "Login successful for user: " + student.getUsername();
    }
    
    // Method to find a student by username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}