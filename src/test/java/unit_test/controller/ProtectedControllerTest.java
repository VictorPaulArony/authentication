package unit_test.controller;

import com.example.authentication.controller.ProtectedController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProtectedControllerTest {

    private ProtectedController controller;

    @BeforeEach
    void setUp() {
        controller = new ProtectedController();
        var auth = new UsernamePasswordAuthenticationToken("user123", null, java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testGetStudentProfile() {
        ResponseEntity<?> response = controller.getStudentProfile();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("user").equals("user123"));
    }

    @Test
    void testGetStudentDashboard() {
        ResponseEntity<?> response = controller.getStudentDashboard();
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("user").equals("user123"));
    }
}

