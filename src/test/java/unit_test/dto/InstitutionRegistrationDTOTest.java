package unit_test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.example.authentication.dto.InstitutionRegistrationDTO;

class InstitutionRegistrationDTOTest {

    @Test
    void testDTOFields() {
        InstitutionRegistrationDTO dto = new InstitutionRegistrationDTO(
                "Green School", "REG123", "PRIMARY", "CBC",
                "Nairobi", "info@school.com", "0700111222", "password123"
        );

        assertEquals("Green School", dto.getSchoolnamne());
        assertEquals("REG123", dto.getRegistraitionNumber());
        assertEquals("PRIMARY", dto.getSchoolType());
        assertEquals("CBC", dto.getEducationSystem());
        assertEquals("Nairobi", dto.getLocation());
        assertEquals("info@school.com", dto.getEmail());
        assertEquals("0700111222", dto.getPhone());
        assertEquals("password123", dto.getPassword());
    }

    @Test
    void testBuilder() {
        InstitutionRegistrationDTO dto = InstitutionRegistrationDTO.builder()
                .schoolnamne("Green School")
                .registraitionNumber("REG123")
                .schoolType("SECONDARY")
                .educationSystem("8-4-4")
                .location("Mombasa")
                .email("admin@greenschool.com")
                .phone("0712345678")
                .password("securepass")
                .build();

        assertEquals("Green School", dto.getSchoolnamne());
        assertEquals("REG123", dto.getRegistraitionNumber());
        assertEquals("SECONDARY", dto.getSchoolType());
        assertEquals("8-4-4", dto.getEducationSystem());
        assertEquals("Mombasa", dto.getLocation());
        assertEquals("admin@greenschool.com", dto.getEmail());
        assertEquals("0712345678", dto.getPhone());
        assertEquals("securepass", dto.getPassword());
    }
}

