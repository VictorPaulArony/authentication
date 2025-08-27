package unit_test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.authentication.dto.LoginRequest;

class LoginRequestTest {

    @Test
    void testFields() {
        LoginRequest req = new LoginRequest("user1", "pass1");

        assertEquals("user1", req.getUsername());
        assertEquals("pass1", req.getPassword());
    }

    @Test
    void testBuilder() {
        LoginRequest req = LoginRequest.builder()
                .username("testuser")
                .password("testpass")
                .build();

        assertEquals("testuser", req.getUsername());
        assertEquals("testpass", req.getPassword());
    }
}

