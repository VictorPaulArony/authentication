package com.example.authentication.dto;

// import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.*;
// import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionRegistrationDTO {

    @NotBlank(message = "School name is required")
    private String schoolnamne;

    @NotBlank(message = "Registration number is required")
    private String registraitionNumber;

    @NotBlank(message = "School type is required")
    private String schoolType;

    @NotBlank(message = "Education system is required")
    private String educationSystem;

    @NotBlank(message = "Location is required")
    private String location;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    // You can add more fields as needed for registration, e.g. principal info, etc.
}

