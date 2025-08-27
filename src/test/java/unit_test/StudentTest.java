package unit_test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.example.authentication.model.Student;

class StudentTest {

    @Test
    void testStudentFields() {
        Student student = new Student(
                1L, "user1",
                "pass",
                "John",
                "Doe",
                "john@example.com",
                "123456789");

        assertEquals(1L, student.getId());
        assertEquals("user1", student.getUsername());
        assertEquals("pass", student.getPassword());
        assertEquals("John", student.getFirstname());
        assertEquals("Doe", student.getLastname());
        assertEquals("john@example.com", student.getEmail());
        assertEquals("123456789", student.getPhone());
    }

    @Test
    void testStudentAuthoritiesIsEmpty() {
        Student student = new Student();
        Collection<?> authorities = student.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
}
