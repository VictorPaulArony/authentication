package unit_test.service;

import com.example.authentication.dto.InstitutionLoginDTO;
import com.example.authentication.dto.InstitutionRegistrationDTO;
import com.example.authentication.model.Institution;
import com.example.authentication.repository.InstitutionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.authentication.service.InstitutionService;

class InstitutionServiceTest {

    private InstitutionRepository repository;
    private PasswordEncoder encoder;
    private InstitutionService service;

    @BeforeEach
    void setUp() {
        repository = mock(InstitutionRepository.class);
        encoder = mock(PasswordEncoder.class);
        service = new InstitutionService(repository, encoder);
    }

    @Test
    void testRegisterSuccess() {
        InstitutionRegistrationDTO dto = InstitutionRegistrationDTO.builder()
                .schoolnamne("School")
                .registraitionNumber("REG123")
                .schoolType("PRIMARY")
                .educationSystem("CBC")
                .location("City")
                .email("test@school.com")
                .phone("0711111111")
                .password("password")
                .build();

        when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(repository.existsByPhone(dto.getPhone())).thenReturn(false);
        when(encoder.encode("password")).thenReturn("encoded");

        Institution saved = Institution.builder().email(dto.getEmail()).build();
        when(repository.save(any())).thenReturn(saved);

        Institution result = service.register(dto);

        assertEquals("test@school.com", result.getEmail());
    }

    @Test
    void testLoginInvalidPassword() {
        Institution institution = new Institution();
        institution.setEmail("test@school.com");
        institution.setPassword("encoded");

        when(repository.findByEmail("test@school.com")).thenReturn(Optional.of(institution));
        when(encoder.matches("wrong", "encoded")).thenReturn(false);

        InstitutionLoginDTO dto = new InstitutionLoginDTO("test@school.com", "wrong");

        assertThrows(IllegalArgumentException.class, () -> service.login(dto));
    }

    @Test
    void testLoadUserByUsername() {
        Institution institution = new Institution();
        institution.setEmail("user@school.com");
        institution.setPassword("pass");

        when(repository.findByEmail("user@school.com")).thenReturn(Optional.of(institution));

        var userDetails = service.loadUserByUsername("user@school.com");

        assertEquals("user@school.com", userDetails.getUsername());
    }
}

