package com.example.authentication.dto;

// import lombok.Data;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
}
