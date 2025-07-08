package com.example.authentication;

import com.example.authentication.controller.AuthController;
import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.model.Student;
import com.example.authentication.model.Teacher;
import com.example.authentication.repository.StudentRepository;
import com.example.authentication.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    private AuthController authController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RegisterRequest studentRequest;
    private TeacherRegisterRequest teacherRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        studentRequest = new RegisterRequest();
        studentRequest.setUsername("integration_student");  // Unique username for each test
        studentRequest.setPassword("integ_pass123");

        teacherRequest = new TeacherRegisterRequest();
        teacherRequest.setUsername("integration_teacher");  // Unique username for each test
        teacherRequest.setPassword("integ_teach123");
        teacherRequest.setEmail("teacher@integration.test");
    }

    @Test
    void whenRegisterAndLoginStudent_thenSuccess() {
        // Register new student
        ResponseEntity<String> registerResponse = authController.registerStudent(studentRequest);
        assertEquals(200, registerResponse.getStatusCode());
        assertTrue(registerResponse.getBody().contains("Registration successful"));

        // Verify student exists in database
        assertTrue(studentRepository.findByUsername(studentRequest.getUsername()).isPresent());
        Student savedStudent = studentRepository.findByUsername(studentRequest.getUsername()).get();
        assertNotNull(savedStudent.getId());
        assertTrue(passwordEncoder.matches(
            studentRequest.getPassword(), 
            savedStudent.getPassword()
        ));

        // Test login with registered student
        ResponseEntity<String> loginResponse = authController.loginStudent(studentRequest);
        assertEquals(200, loginResponse.getStatusCode());
        assertTrue(loginResponse.getBody().contains("Login successful"));
    }

    @Test
    void whenRegisterAndLoginTeacher_thenSuccess() {
        // Register new teacher
        ResponseEntity<String> registerResponse = authController.registerTeacher(teacherRequest);
        assertEquals(200, registerResponse.getStatusCode());
        assertTrue(registerResponse.getBody().contains("Teacher registration successful"));

        // Verify teacher exists in database
        assertTrue(teacherRepository.findByUsername(teacherRequest.getUsername()).isPresent());
        Teacher savedTeacher = teacherRepository.findByUsername(teacherRequest.getUsername()).get();
        assertNotNull(savedTeacher.getId());
        assertTrue(passwordEncoder.matches(
            teacherRequest.getPassword(), 
            savedTeacher.getPassword()
        ));
        assertEquals(teacherRequest.getEmail(), savedTeacher.getEmail());

        // Test login with registered teacher
        ResponseEntity<String> loginResponse = authController.loginTeacher(teacherRequest);
        assertEquals(200, loginResponse.getStatusCode());
        assertTrue(loginResponse.getBody().contains("Login successful"));
    }

    @Test
    void whenRegisterWithExistingUsername_thenFail() {
        // First registration should succeed
        ResponseEntity<String> firstRegister = authController.registerStudent(studentRequest);
        assertEquals(200, firstRegister.getStatusCode());

        // Second registration with same username should fail
        RegisterRequest duplicateRequest = new RegisterRequest();
        duplicateRequest.setUsername(studentRequest.getUsername());
        duplicateRequest.setPassword("different_password");
        
        ResponseEntity<String> secondRegister = authController.registerStudent(duplicateRequest);
        assertEquals(400, secondRegister.getStatusCode());
        assertTrue(secondRegister.getBody().contains("already exists"));
    }

    @Test
    void whenLoginWithInvalidCredentials_thenFail() {
        // Register a student first
        authController.registerStudent(studentRequest);

        // Test 1: Wrong password
        RegisterRequest wrongPasswordRequest = new RegisterRequest();
        wrongPasswordRequest.setUsername(studentRequest.getUsername());
        wrongPasswordRequest.setPassword("wrong_password");
        
        ResponseEntity<String> wrongPassResponse = authController.loginStudent(wrongPasswordRequest);
        assertEquals(400, wrongPassResponse.getStatusCode());
        assertTrue(wrongPassResponse.getBody().contains("Invalid password"));
        
        // Test 2: Non-existent user
        RegisterRequest nonExistentUserRequest = new RegisterRequest();
        nonExistentUserRequest.setUsername("nonexistent_user");
        nonExistentUserRequest.setPassword("any_password");
        
        ResponseEntity<String> nonExistentResponse = authController.loginStudent(nonExistentUserRequest);
        assertEquals(400, nonExistentResponse.getStatusCode());
        assertTrue(nonExistentResponse.getBody().contains("User not found"));
    }
}
