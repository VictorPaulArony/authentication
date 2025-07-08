package com.example.authentication;

import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.repository.StudentRepository;
import com.example.authentication.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ApiTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private String baseUrl;
    private RegisterRequest studentRequest;
    private TeacherRegisterRequest teacherRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
        
        // Setup test data
        studentRequest = new RegisterRequest();
        studentRequest.setUsername("api_test_student");
        studentRequest.setPassword("api_pass123");

        teacherRequest = new TeacherRegisterRequest();
        teacherRequest.setUsername("api_test_teacher");
        teacherRequest.setPassword("api_teach123");
        teacherRequest.setEmail("api_teacher@test.com");
    }

    @AfterEach
    void tearDown() {
        // Clean up any test data after each test
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    private <T> HttpEntity<T> createJsonRequest(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    @Test
    void whenRegisterStudent_thenSuccess() {
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/student/register",
            HttpMethod.POST,
            createJsonRequest(studentRequest),
            String.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Registration successful"));
        
        // Verify student was saved to database
        assertTrue(studentRepository.findByUsername(studentRequest.getUsername()).isPresent());
    }

    @Test
    void whenLoginStudent_thenSuccess() {
        // Given
        restTemplate.postForEntity(
            baseUrl + "/student/register",
            createJsonRequest(studentRequest),
            String.class
        );

        // When
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/student/login",
            HttpMethod.POST,
            createJsonRequest(studentRequest),
            String.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Login successful"));
    }

    @Test
    void whenRegisterTeacher_thenSuccess() {
        // When
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/teacher/register",
            HttpMethod.POST,
            createJsonRequest(teacherRequest),
            String.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Teacher registration successful"));
        
        // Verify teacher was saved to database
        assertTrue(teacherRepository.findByUsername(teacherRequest.getUsername()).isPresent());
    }

    @Test
    void whenLoginTeacher_thenSuccess() {
        // Given
        restTemplate.postForEntity(
            baseUrl + "/teacher/register",
            createJsonRequest(teacherRequest),
            String.class
        );

        // When
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/teacher/login",
            HttpMethod.POST,
            createJsonRequest(teacherRequest),
            String.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Login successful"));
    }

    @Test
    void whenRegisterWithExistingUsername_thenFail() {
        // Given - register first time
        restTemplate.postForEntity(
            baseUrl + "/student/register",
            createJsonRequest(studentRequest),
            String.class
        );

        // When - try to register with same username
        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/student/register",
            HttpMethod.POST,
            createJsonRequest(studentRequest),
            String.class
        );

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("already exists"));
    }

    @Test
    void whenLoginWithInvalidCredentials_thenFail() {
        // Given - register a student
        restTemplate.postForEntity(
            baseUrl + "/student/register",
            createJsonRequest(studentRequest),
            String.class
        );

        // When - try to login with wrong password
        RegisterRequest wrongPasswordRequest = new RegisterRequest();
        wrongPasswordRequest.setUsername(studentRequest.getUsername());
        wrongPasswordRequest.setPassword("wrong_password");

        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/student/login",
            HttpMethod.POST,
            createJsonRequest(wrongPasswordRequest),
            String.class
        );

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid password"));
    }

    @Test
    void whenLoginWithNonExistentUser_thenFail() {
        // Given - no user registered
        
        // When - try to login with non-existent user
        RegisterRequest nonExistentUser = new RegisterRequest();
        nonExistentUser.setUsername("nonexistent");
        nonExistentUser.setPassword("anypassword");

        ResponseEntity<String> response = restTemplate.exchange(
            baseUrl + "/student/login",
            HttpMethod.POST,
            createJsonRequest(nonExistentUser),
            String.class
        );

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("User not found"));
    }

    @Test
    void whenInvalidEndpoint_thenReturn403() {
        // When - accessing a non-existent endpoint
        ResponseEntity<String> response = restTemplate.getForEntity(
            baseUrl + "/nonexistent",
            String.class
        );

        // Then - expect 403 FORBIDDEN as Spring Security blocks access to unauthorized endpoints
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
