package unit_test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.authentication.dto.InstitutionLoginDTO;

class InstitutionLoginDTOTest {

    @Test
    void testDTOFields() {
        InstitutionLoginDTO dto = new InstitutionLoginDTO("school@example.com", "secret");

        assertEquals("school@example.com", dto.getEmail());
        assertEquals("secret", dto.getPassword());
    }

    @Test
    void testBuilder() {
        InstitutionLoginDTO dto = InstitutionLoginDTO.builder()
                .email("admin@school.com")
                .password("admin123")
                .build();

        assertEquals("admin@school.com", dto.getEmail());
        assertEquals("admin123", dto.getPassword());
    }
}

