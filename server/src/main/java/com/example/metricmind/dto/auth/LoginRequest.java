package com.example.metricmind.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "idToken is required")
    private String idToken;
}
