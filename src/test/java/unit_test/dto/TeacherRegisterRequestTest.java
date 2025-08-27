package unit_test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.authentication.dto.TeacherRegisterRequest;

class TeacherRegisterRequestTest {

    @Test
    void testFields() {
        TeacherRegisterRequest req = new TeacherRegisterRequest(
                "teach1", "password", "teach@example.com", "Math");

        assertEquals("teach1", req.getUsername());
        assertEquals("password", req.getPassword());
        assertEquals("teach@example.com", req.getEmail());
        assertEquals("Math", req.getSubject());
    }

    @Test
    void testBuilder() {
        TeacherRegisterRequest req = TeacherRegisterRequest.builder()
                .username("mrsmith")
                .password("smithpass")
                .email("smith@school.com")
                .subject("Physics")
                .build();

        assertEquals("mrsmith", req.getUsername());
        assertEquals("Physics", req.getSubject());
    }
}

