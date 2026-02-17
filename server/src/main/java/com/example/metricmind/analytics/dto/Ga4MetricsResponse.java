package com.example.metricmind.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Ga4MetricsResponse {
    private String propertyId;
    private String dateRange;
    private Map<String, Object> metrics;
    private Map<String, Object> dimensions;
}