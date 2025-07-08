package com.example.authentication;

import com.example.authentication.controller.AuthController;
import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.service.StudentService;
import com.example.authentication.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationApplicationTests {

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest studentRequest;
    private TeacherRegisterRequest teacherRequest;

    @BeforeEach
    void setUp() {
        studentRequest = new RegisterRequest();
        studentRequest.setUsername("teststudent");
        studentRequest.setPassword("password123");

        teacherRequest = new TeacherRegisterRequest();
        teacherRequest.setUsername("testteacher");
        teacherRequest.setPassword("password123");
        teacherRequest.setEmail("teacher@school.edu");
    }

    // Student Registration Tests
    @Test
    void whenRegisterStudent_thenReturnSuccessMessage() {
        // Arrange
        String successMessage = "Registration successful for user: teststudent";
        when(studentService.register(any(RegisterRequest.class))).thenReturn(successMessage);

        // Act
        ResponseEntity<String> response = authController.registerStudent(studentRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successMessage, response.getBody());
        verify(studentService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void whenRegisterStudentWithExistingUsername_thenReturnBadRequest() {
        // Arrange
        String errorMessage = "Username already exists";
        when(studentService.register(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<String> response = authController.registerStudent(studentRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(errorMessage));
    }

    // Student Login Tests
    @Test
    void whenLoginStudent_thenReturnSuccessMessage() {
        // Arrange
        String successMessage = "Login successful for user: teststudent";
        when(studentService.login(any(RegisterRequest.class))).thenReturn(successMessage);

        // Act
        ResponseEntity<String> response = authController.loginStudent(studentRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successMessage, response.getBody());
    }

    @Test
    void whenLoginWithInvalidCredentials_thenReturnBadRequest() {
        // Arrange
        String errorMessage = "Invalid credentials";
        when(studentService.login(any(RegisterRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<String> response = authController.loginStudent(studentRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(errorMessage));
    }

    // Teacher Registration Tests
    @Test
    void whenRegisterTeacher_thenReturnSuccessMessage() {
        // Arrange
        String successMessage = "Teacher registration successful for: testteacher";
        when(teacherService.register(any(TeacherRegisterRequest.class))).thenReturn(successMessage);

        // Act
        ResponseEntity<String> response = authController.registerTeacher(teacherRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successMessage, response.getBody());
        verify(teacherService, times(1)).register(any(TeacherRegisterRequest.class));
    }

    @Test
    void whenRegisterTeacherWithExistingUsername_thenReturnBadRequest() {
        // Arrange
        String errorMessage = "Username already exists";
        when(teacherService.register(any(TeacherRegisterRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<String> response = authController.registerTeacher(teacherRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(errorMessage));
    }

    // Teacher Login Tests
    @Test
    void whenLoginTeacher_thenReturnSuccessMessage() {
        // Arrange
        String successMessage = "Login successful for teacher: testteacher";
        when(teacherService.login(any(TeacherRegisterRequest.class))).thenReturn(successMessage);

        // Act
        ResponseEntity<String> response = authController.loginTeacher(teacherRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successMessage, response.getBody());
    }

    @Test
    void whenLoginTeacherWithInvalidCredentials_thenReturnBadRequest() {
        // Arrange
        String errorMessage = "Invalid credentials";
        when(teacherService.login(any(TeacherRegisterRequest.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<String> response = authController.loginTeacher(teacherRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(errorMessage));
    }
}

