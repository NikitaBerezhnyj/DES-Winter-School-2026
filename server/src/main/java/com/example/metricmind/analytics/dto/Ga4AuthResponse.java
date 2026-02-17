package com.example.metricmind.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Ga4AuthResponse {
    private boolean success;
    private String message;
    private LocalDateTime tokenExpiresAt;
}