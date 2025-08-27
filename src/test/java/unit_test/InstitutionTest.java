package unit_test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.example.authentication.model.Institution;
import com.example.authentication.model.Institution.AccreditationStatus;
import com.example.authentication.model.Institution.EducationSystem;
import com.example.authentication.model.Institution.SchoolType;
import com.example.authentication.model.Teacher;

class InstitutionTest {

    @Test
    void testInstitutionBuilderAndFields() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setUsername("creator");

        LocalDateTime now = LocalDateTime.now();

        Institution institution = Institution.builder()
            .schoolnamne("Green School")
            .registraitionNumber("REG123")
            .schoolType(SchoolType.PRIMARY)
            .educationSystem(EducationSystem.CBC)
            .location("Nairobi")
            .email("school@example.com")
            .phone("0700111222")
            .password("adminpass")
            .postalAddress("P.O. Box 456")
            .website("https://greenschool.ke")
            .description("A premier institution")
            .principalName("Jane Doe")
            .principalEmail("jane@school.com")
            .principalPhone("0712345678")
            .establishedYear(2005)
            .accreditationBody("MoE")
            .accreditationDate(now)
            .createdBy(teacher)
            .build();

        assertEquals("Green School", institution.getSchoolnamne());
        assertEquals("REG123", institution.getRegistraitionNumber());
        assertEquals(SchoolType.PRIMARY, institution.getSchoolType());
        assertEquals(EducationSystem.CBC, institution.getEducationSystem());
        assertEquals("Nairobi", institution.getLocation());
        assertEquals("school@example.com", institution.getEmail());
        assertEquals("0700111222", institution.getPhone());
        assertEquals("adminpass", institution.getPassword());
        assertEquals("P.O. Box 456", institution.getPostalAddress());
        assertEquals("https://greenschool.ke", institution.getWebsite());
        assertEquals("A premier institution", institution.getDescription());
        assertEquals("Jane Doe", institution.getPrincipalName());
        assertEquals("jane@school.com", institution.getPrincipalEmail());
        assertEquals("0712345678", institution.getPrincipalPhone());
        assertEquals(2005, institution.getEstablishedYear());
        assertEquals("MoE", institution.getAccreditationBody());
        assertEquals(now, institution.getAccreditationDate());
        assertEquals(teacher, institution.getCreatedBy());
        assertNotNull(institution.getCreatedAt());
        assertEquals(AccreditationStatus.PENDING, institution.getAccreditationStatus());
    }

    @Test
    void testDefaultAccreditationStatus() {
        Institution institution = new Institution();
        assertEquals(AccreditationStatus.PENDING, institution.getAccreditationStatus());
    }
}

