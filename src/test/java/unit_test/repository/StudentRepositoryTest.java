package unit_test.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.authentication.model.Student;
import com.example.authentication.repository.StudentRepository;

class StudentRepositoryTest {

    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
    }

    @Test
    void testFindByUsername() {
        Student student = new Student();
        student.setUsername("john123");

        when(studentRepository.findByUsername("john123"))
                .thenReturn(Optional.of(student));

        Optional<Student> result = studentRepository.findByUsername("john123");

        assertTrue(result.isPresent());
        assertEquals("john123", result.get().getUsername());
        verify(studentRepository).findByUsername("john123");
    }
}