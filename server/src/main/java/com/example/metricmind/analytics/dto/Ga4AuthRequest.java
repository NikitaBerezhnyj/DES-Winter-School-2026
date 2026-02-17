package com.example.metricmind.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Ga4AuthRequest {
    @NotBlank(message = "Authorization code is required")
    private String code;
}