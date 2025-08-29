package unit_test.service;

import com.example.authentication.dto.RegisterRequest;
import com.example.authentication.model.Student;
import com.example.authentication.repository.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.authentication.service.StudentService;

class StudentServiceTest {

    private StudentRepository repository;
    private PasswordEncoder encoder;
    private StudentService service;

    @BeforeEach
    void setUp() {
        repository = mock(StudentRepository.class);
        encoder = mock(PasswordEncoder.class);
        service = new StudentService(repository, encoder);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest req = RegisterRequest.builder()
                .username("john")
                .password("123456")
                .firstname("John")
                .lastname("Doe")
                .email("john@example.com")
                .phone("0711223344")
                .build();

        when(repository.findByUsername("john")).thenReturn(Optional.empty());
        when(encoder.encode("123456")).thenReturn("encoded");

        String result = service.register(req);
        assertTrue(result.contains("john"));
    }

    @Test
    void testLoginFailsOnWrongPassword() {
        Student student = new Student();
        student.setUsername("john");
        student.setPassword("encoded");

        when(repository.findByUsername("john")).thenReturn(Optional.of(student));
        when(encoder.matches("wrong", "encoded")).thenReturn(false);

        RegisterRequest req = RegisterRequest.builder()
                .username("john")
                .password("wrong")
                .build();

        assertThrows(RuntimeException.class, () -> service.login(req));
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        Student student = new Student();
        student.setUsername("john");

        when(repository.findByUsername("john")).thenReturn(Optional.of(student));

        var result = service.loadUserByUsername("john");
        assertEquals("john", result.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(repository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("unknown"));
    }
}

