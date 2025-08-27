package com.example.authentication.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authentication.component.JwtUtil;
import com.example.authentication.dto.InstitutionLoginDTO;
import com.example.authentication.dto.InstitutionRegistrationDTO;
import com.example.authentication.model.Institution;
import com.example.authentication.service.InstitutionService;
import com.example.authentication.service.RateLimiterService;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class InstitutionController {

    private final AuthenticationManager authenticationManager;

    private final InstitutionService institutionService;

    private final RateLimiterService rateLimiterService;

    private final JwtUtil jwtUtil;

    private String generateKey(HttpServletRequest request, String endpointName) {
        String ip = request.getRemoteAddr();
        return ip + ":" + endpointName;
    }

    @PostMapping("/register/institution")
    public ResponseEntity<?> registerInstitution(HttpServletRequest request,
            @Valid @RequestBody InstitutionRegistrationDTO registrationDTO) {
        String key = generateKey(request, "/api/auth");
        Bucket bucket = rateLimiterService.resolveBucket(key, 5, Duration.ofMinutes(10));

        if (!bucket.tryConsume(1)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Too many registration attempts. Please try again later.");
            return ResponseEntity.status(429).body(error);
        }

        try {
            Institution savedInstitution = institutionService.register(registrationDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Institution registered successfully");
            response.put("institution", savedInstitution.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error ", "Institution registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login/institution")
    public ResponseEntity<?> loginInstitution(HttpServletRequest request,
            @Valid @RequestBody InstitutionLoginDTO loginDTO) {
        String key = generateKey(request, "login-institution");
        Bucket bucket = rateLimiterService.resolveBucket(key, 5, Duration.ofMinutes(5)); // Customize as needed

        if (!bucket.tryConsume(1)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Too many login attempts. Please try again later.");
            return ResponseEntity.status(429).body(error);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            UserDetails userDetails = institutionService.loadUserByUsername(loginDTO.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", loginDTO.getEmail());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
}
