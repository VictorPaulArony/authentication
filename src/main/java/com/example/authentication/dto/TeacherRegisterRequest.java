package com.example.authentication.dto;

import lombok.Data;

@Data
public class TeacherRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String subject;
}
