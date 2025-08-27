package unit_test.controller;

import com.example.authentication.component.JwtUtil;
import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.service.StudentService;
import com.example.authentication.service.TeacherService;
import com.example.authentication.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthenticationManager authManager;
    private StudentService studentService;
    private TeacherService teacherService;
    private JwtUtil jwtUtil;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthenticationManager.class);
        studentService = mock(StudentService.class);
        teacherService = mock(TeacherService.class);
        jwtUtil = mock(JwtUtil.class);
        controller = new AuthController(authManager, studentService, teacherService, jwtUtil);
    }

    @Test
    void testRegisterStudentSuccess() {
        RegisterRequest request = RegisterRequest.builder().username("john").build();
        when(studentService.register(request)).thenReturn("Success");

        var response = controller.registerStudent(request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testLoginStudentInvalid() {
        RegisterRequest request = RegisterRequest.builder().username("john").password("123").build();
        doThrow(new BadCredentialsException("Bad credentials")).when(authManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        var response = controller.loginStudent(request);
        assertEquals(401, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
    }

    @Test
    void testRegisterTeacherSuccess() {
        TeacherRegisterRequest request = TeacherRegisterRequest.builder().username("teach1").build();
        when(teacherService.register(request)).thenReturn("Registered");

        var response = controller.registerTeacher(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Registered", response.getBody());
    }

    @Test
    void testLoginTeacherSuccess() {
        TeacherRegisterRequest request = TeacherRegisterRequest.builder().username("teach1").password("123").build();
        when(jwtUtil.generateToken("teach1")).thenReturn("token123");

        var response = controller.loginTeacher(request);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("token"));
    }
}
