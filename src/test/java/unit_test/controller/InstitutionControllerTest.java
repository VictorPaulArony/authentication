package unit_test.controller;

import com.example.authentication.component.JwtUtil;
import com.example.authentication.dto.InstitutionLoginDTO;
import com.example.authentication.dto.InstitutionRegistrationDTO;
import com.example.authentication.model.Institution;
import com.example.authentication.service.InstitutionService;
import com.example.authentication.service.RateLimiterService;
import com.example.authentication.controller.InstitutionController;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstitutionControllerTest {

    private AuthenticationManager authManager;
    private InstitutionService institutionService;
    private RateLimiterService rateLimiterService;
    private JwtUtil jwtUtil;
    private InstitutionController controller;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthenticationManager.class);
        institutionService = mock(InstitutionService.class);
        rateLimiterService = mock(RateLimiterService.class);
        jwtUtil = mock(JwtUtil.class);
        controller = new InstitutionController(authManager, institutionService, rateLimiterService, jwtUtil);
        request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    }

    @Test
    void testRegisterInstitutionSuccess() {
        InstitutionRegistrationDTO dto = InstitutionRegistrationDTO.builder()
                .schoolnamne("School")
                .registraitionNumber("123")
                .schoolType("PRIMARY")
                .educationSystem("CBC")
                .location("City")
                .email("inst@example.com")
                .phone("0700000000")
                .password("123456")
                .build();

        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(true);
        when(rateLimiterService.resolveBucket(any(), anyInt(), any())).thenReturn(bucket);

        Institution institution = new Institution();
        institution.setEmail("inst@example.com");

        when(institutionService.register(dto)).thenReturn(institution);

        var response = controller.registerInstitution(request, dto);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testLoginInstitutionTooManyAttempts() {
        InstitutionLoginDTO dto = new InstitutionLoginDTO("inst@example.com", "123456");
        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(false);
        when(rateLimiterService.resolveBucket(any(), anyInt(), any())).thenReturn(bucket);

        var response = controller.loginInstitution(request, dto);
        assertEquals(429, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("error"));
    }

    @Test
    void testLoginInstitutionSuccess() {
        InstitutionLoginDTO dto = new InstitutionLoginDTO("inst@example.com", "123456");

        Bucket bucket = mock(Bucket.class);
        when(bucket.tryConsume(1)).thenReturn(true);
        when(rateLimiterService.resolveBucket(any(), anyInt(), any())).thenReturn(bucket);

        User user = new User("inst@example.com", "pass", java.util.Collections.emptyList());
        when(institutionService.loadUserByUsername("inst@example.com")).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("jwt-token");

        var response = controller.loginInstitution(request, dto);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("token"));
    }
}

