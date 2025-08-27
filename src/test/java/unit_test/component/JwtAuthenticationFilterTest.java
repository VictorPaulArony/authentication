package unit_test.component;
import com.example.authentication.component.JwtUtil;
import com.example.authentication.component.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtUtil jwtUtil;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
    }

    @Test
    void doFilterInternal_withValidToken_shouldAuthenticate() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "valid-token";
        String username = "john";
        UserDetails userDetails = new User(username, "password", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).loadUserByUsername(username);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withNoToken_shouldSkipAuthentication() throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, never()).extractUsername(any());
        verify(filterChain).doFilter(request, response);
    }
}

