package com.example.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {
    
    @GetMapping("/student/profile")
    public ResponseEntity<?> getStudentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is a protected student endpoint");
        response.put("user", username);
        response.put("authorities", authentication.getAuthorities());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/student/dashboard")
    public ResponseEntity<?> getStudentDashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to your student dashboard!");
        response.put("user", authentication.getName());
        
        return ResponseEntity.ok(response);
    }
}