package unit_test.config;

import com.example.authentication.config.ApplicationConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigTest {

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        ApplicationConfig config = new ApplicationConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder.matches("password", encoder.encode("password")));
    }
}

