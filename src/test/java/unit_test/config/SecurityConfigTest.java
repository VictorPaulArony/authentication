package unit_test.config;
import com.example.authentication.config.SecurityConfig;

import com.example.authentication.component.JwtAuthenticationFilter;
import com.example.authentication.service.CustomUserDetailsService;
import com.example.authentication.service.InstitutionService;
import com.example.authentication.service.StudentService;
import com.example.authentication.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private StudentService studentService;
    private TeacherService teacherService;
    private InstitutionService institutionService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private PasswordEncoder passwordEncoder;
    private CustomUserDetailsService customUserDetailsService;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        teacherService = mock(TeacherService.class);
        institutionService = mock(InstitutionService.class);
        jwtAuthenticationFilter = mock(JwtAuthenticationFilter.class);
        passwordEncoder = mock(PasswordEncoder.class);
        customUserDetailsService = mock(CustomUserDetailsService.class);

        securityConfig = new SecurityConfig(
                studentService,
                teacherService,
                institutionService,
                jwtAuthenticationFilter,
                passwordEncoder,
                customUserDetailsService
        );
    }

    @Test
    void configureGlobal_shouldNotThrowException() {
        AuthenticationManagerBuilder builder = mock(AuthenticationManagerBuilder.class);
        when(builder.userDetailsService(any())).thenReturn(builder);

        assertDoesNotThrow(() -> securityConfig.configureGlobal(builder));
    }
}

