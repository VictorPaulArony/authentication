package unit_test.service;

import com.example.authentication.dto.TeacherRegisterRequest;
import com.example.authentication.service.TeacherService;
import com.example.authentication.model.Teacher;
import com.example.authentication.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    private TeacherRepository repository;
    private PasswordEncoder encoder;
    private TeacherService service;

    @BeforeEach
    void setUp() {
        repository = mock(TeacherRepository.class);
        encoder = mock(PasswordEncoder.class);
        service = new TeacherService(repository, encoder);
    }

    @Test
    void testRegisterSuccess() {
        TeacherRegisterRequest req = TeacherRegisterRequest.builder()
                .username("teach1")
                .password("123456")
                .email("teach@example.com")
                .subject("Math")
                .build();

        when(repository.existsByUsername("teach1")).thenReturn(false);
        when(encoder.encode("123456")).thenReturn("encoded");

        String result = service.register(req);
        assertTrue(result.contains("teach1"));
    }

    @Test
    void testLoginFailsInvalidPassword() {
        Teacher teacher = new Teacher();
        teacher.setUsername("teach1");
        teacher.setPassword("encoded");

        when(repository.findByUsername("teach1")).thenReturn(Optional.of(teacher));
        when(encoder.matches("wrong", "encoded")).thenReturn(false);

        TeacherRegisterRequest req = TeacherRegisterRequest.builder()
                .username("teach1")
                .password("wrong")
                .build();

        assertThrows(RuntimeException.class, () -> service.login(req));
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        Teacher teacher = new Teacher();
        teacher.setUsername("teach1");

        when(repository.findByUsername("teach1")).thenReturn(Optional.of(teacher));

        var result = service.loadUserByUsername("teach1");
        assertEquals("teach1", result.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(repository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("unknown"));
    }
}

