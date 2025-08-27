package unit_test.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.authentication.model.Institution;
import com.example.authentication.repository.InstitutionRepository;

class InstitutionRepositoryTest {

    private InstitutionRepository institutionRepository;

    @BeforeEach
    void setUp() {
        institutionRepository = mock(InstitutionRepository.class);
    }

    @Test
    void testFindByEmail() {
        Institution institution = new Institution();
        institution.setEmail("test@school.com");

        when(institutionRepository.findByEmail("test@school.com"))
                .thenReturn(Optional.of(institution));

        Optional<Institution> result = institutionRepository.findByEmail("test@school.com");

        assertTrue(result.isPresent());
        assertEquals("test@school.com", result.get().getEmail());
        verify(institutionRepository).findByEmail("test@school.com");
    }

    @Test
    void testExistsByEmail() {
        when(institutionRepository.existsByEmail("exists@school.com")).thenReturn(true);

        boolean exists = institutionRepository.existsByEmail("exists@school.com");

        assertTrue(exists);
        verify(institutionRepository).existsByEmail("exists@school.com");
    }

    @Test
    void testExistsByPhone() {
        when(institutionRepository.existsByPhone("0712345678")).thenReturn(true);

        boolean exists = institutionRepository.existsByPhone("0712345678");

        assertTrue(exists);
        verify(institutionRepository).existsByPhone("0712345678");
    }
}
