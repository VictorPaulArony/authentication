package unit_test.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.authentication.dto.RegisterRequest;

class RegisterRequestTest {

    @Test
    void testFields() {
        RegisterRequest req = new RegisterRequest(
                "user2", "pass2", "John", "Doe", "john@ex.com", "0711223344");

        assertEquals("user2", req.getUsername());
        assertEquals("pass2", req.getPassword());
        assertEquals("John", req.getFirstname());
        assertEquals("Doe", req.getLastname());
        assertEquals("john@ex.com", req.getEmail());
        assertEquals("0711223344", req.getPhone());
    }

    @Test
    void testBuilder() {
        RegisterRequest req = RegisterRequest.builder()
                .username("newuser")
                .password("newpass")
                .firstname("Jane")
                .lastname("Doe")
                .email("jane@example.com")
                .phone("0700123456")
                .build();

        assertEquals("Jane", req.getFirstname());
        assertEquals("jane@example.com", req.getEmail());
    }
}

