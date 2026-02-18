package com.example.metricmind.auth.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "idToken is required")
    private String idToken;
}
