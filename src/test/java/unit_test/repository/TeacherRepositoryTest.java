package unit_test.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.authentication.model.Teacher;
import com.example.authentication.repository.TeacherRepository;

class TeacherRepositoryTest {

    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherRepository = mock(TeacherRepository.class);
    }

    @Test
    void testFindByUsername() {
        Teacher teacher = new Teacher();
        teacher.setUsername("teacher1");

        when(teacherRepository.findByUsername("teacher1"))
                .thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherRepository.findByUsername("teacher1");

        assertTrue(result.isPresent());
        assertEquals("teacher1", result.get().getUsername());
        verify(teacherRepository).findByUsername("teacher1");
    }

    @Test
    void testExistsByUsername() {
        when(teacherRepository.existsByUsername("mrsmith")).thenReturn(true);

        boolean exists = teacherRepository.existsByUsername("mrsmith");

        assertTrue(exists);
        verify(teacherRepository).existsByUsername("mrsmith");
    }
}

