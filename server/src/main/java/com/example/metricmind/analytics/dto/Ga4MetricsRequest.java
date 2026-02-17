package com.example.metricmind.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Ga4MetricsRequest {
    @NotBlank(message = "Property ID is required")
    private String propertyId;
    
    private String startDate = "7daysAgo";
    private String endDate = "today";
}