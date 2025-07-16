package com.example.authentication.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authentication.component.JwtUtil;
import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.service.StudentService;
import com.example.authentication.service.TeacherService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // Student endpoints
    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterRequest request) {
        try {
            String message = studentService.register(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", message);
            response.put("user", request.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Student registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/login/student")
    public ResponseEntity<?> loginStudent(@RequestBody RegisterRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            
            UserDetails userDetails = studentService.loadUserByUsername(request.getUsername());
            final String token = jwtUtil.generateToken(userDetails);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", request.getUsername());
            
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    // Teacher endpoints
    @PostMapping("/register/teacher")
    public ResponseEntity<String> registerTeacher(@RequestBody TeacherRegisterRequest request) {
        try {
            String message = teacherService.register(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Teacher registration failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/login/teacher")
    public ResponseEntity<Map<String, String>> loginTeacher(@RequestBody TeacherRegisterRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            final String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}