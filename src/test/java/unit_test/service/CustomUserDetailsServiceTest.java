package unit_test.service;

import com.example.authentication.model.Student;
import com.example.authentication.model.Teacher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.authentication.service.CustomUserDetailsService;
import com.example.authentication.service.StudentService;
import com.example.authentication.service.TeacherService;

class CustomUserDetailsServiceTest {

    private StudentService studentService;
    private TeacherService teacherService;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        teacherService = mock(TeacherService.class);
        userDetailsService = new CustomUserDetailsService(studentService, teacherService);
    }

    @Test
    void testLoadUserFromStudentService() {
        Student student = new Student();
        student.setUsername("john");

        when(studentService.loadUserByUsername("john")).thenReturn(student);

        assertEquals(student, userDetailsService.loadUserByUsername("john"));
    }

    @Test
    void testFallbackToTeacherService() {
        when(studentService.loadUserByUsername("john"))
                .thenThrow(new UsernameNotFoundException("Not found"));

        Teacher teacher = new Teacher();
        teacher.setUsername("john");

        when(teacherService.loadUserByUsername("john")).thenReturn(teacher);

        assertEquals(teacher, userDetailsService.loadUserByUsername("john"));
    }
}

