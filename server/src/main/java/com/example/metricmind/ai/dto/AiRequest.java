package com.example.metricmind.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AiRequest {
    private String period;
    private Map<String, Object> metrics;
}
