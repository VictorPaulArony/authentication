package com.example.authentication.controller;

import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.service.StudentService;
import com.example.authentication.service.TeacherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;

    // Student endpoints
    @PostMapping("/student/register")
    public ResponseEntity<String> registerStudent(@RequestBody RegisterRequest request) {
        try {
            String message = studentService.register(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Student registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/student/login")
    public ResponseEntity<String> loginStudent(@RequestBody RegisterRequest request) {
        try {
            String message = studentService.login(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Student login failed: " + e.getMessage());
        }
    }
    
    // Teacher endpoints
    @PostMapping("/teacher/register")
    public ResponseEntity<String> registerTeacher(@RequestBody TeacherRegisterRequest request) {
        try {
            String message = teacherService.register(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Teacher registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/teacher/login")
    public ResponseEntity<String> loginTeacher(@RequestBody TeacherRegisterRequest request) {
        try {
            String message = teacherService.login(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Teacher login failed: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}
