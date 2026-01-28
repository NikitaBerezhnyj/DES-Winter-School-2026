package com.example.metricmind.dto.analytics;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class DashboardDto {
    private Metrics metrics;
    
    private AiReport aiReport;

    @Data
    @Builder
    public static class Metrics {
        private int users;
        private int sessions;
        private int eventCount;
        private int conversions;
        private double engagementTime;
        private Map<String, Integer> trafficSource;
    }

    @Data
    @Builder
    public static class AiReport {
        private String summary;
        private String explanation;
        private String recommendation;
    }
}
