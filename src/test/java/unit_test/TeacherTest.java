package unit_test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.authentication.model.Teacher;

class TeacherTest {

    @Test
    void testTeacherFields() {
        Teacher teacher = new Teacher();
        teacher.setId(2L);
        teacher.setUsername("teacher1");
        teacher.setEmail("teach@example.com");
        teacher.setPassword("securepass");
        teacher.setSubject("Math");

        assertEquals(2L, teacher.getId());
        assertEquals("teacher1", teacher.getUsername());
        assertEquals("teach@example.com", teacher.getEmail());
        assertEquals("securepass", teacher.getPassword());
        assertEquals("Math", teacher.getSubject());
    }

    @Test
    void testTeacherAuthoritiesIsEmpty() {
        Teacher teacher = new Teacher();
        Collection<?> authorities = teacher.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
}

