package com.example.metricmind.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ga4PropertyDto {
    private String propertyId;
    private String displayName;
    private String industryCategory;
    private String timeZone;
    private String currencyCode;
}