package com.example.metricmind.dto.ai;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AiRequest {
    private String period;
    private Map<String, Object> metrics;
}
