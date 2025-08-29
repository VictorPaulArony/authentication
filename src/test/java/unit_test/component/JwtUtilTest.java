package unit_test.component;

import com.example.authentication.component.JwtUtil;

import com.example.authentication.service.StudentService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        jwtUtil = new JwtUtil(studentService);
        // Simulate @Value injection
        jwtUtil = Mockito.spy(jwtUtil);
        doReturn("mysecretkey123456789012345678901234").when(jwtUtil).getSecret();
        doReturn(1000L * 60 * 60 * 24).when(jwtUtil).getExpiration(); // 24h
    }

    @Test
    void generateAndValidateToken_withValidUser_shouldSucceed() {
        UserDetails user = new User("john_doe", "password", new java.util.ArrayList<>());

        String token = jwtUtil.generateToken(user);
        assertNotNull(token);

        String username = jwtUtil.extractUsername(token);
        assertEquals("john_doe", username);

        boolean isValid = jwtUtil.validateToken(token, user);
        assertTrue(isValid);
    }

    @Test
    void tokenShouldBeExpired_afterSettingPastExpiration() throws InterruptedException {
        UserDetails user = new User("expired_user", "password", new java.util.ArrayList<>());
        doReturn(1L).when(jwtUtil).getExpiration(); // 1ms expiration

        String token = jwtUtil.generateToken(user);
        Thread.sleep(10); // Ensure token expires

        assertTrue(jwtUtil.isTokenExpired(token));
    }

    @Test
    void extractClaim_shouldReturnCorrectSubject() {
        String token = jwtUtil.generateToken("test_subject");
        String subject = jwtUtil.extractClaim(token, Claims::getSubject);
        assertEquals("test_subject", subject);
    }

    // Helpers for mocked @Value fields
    private String getSecret() {
        return "mysecretkey123456789012345678901234";
    }

    private Long getExpiration() {
        return 1000L * 60 * 60 * 24;
    }
}

