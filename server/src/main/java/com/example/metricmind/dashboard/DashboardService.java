package com.example.metricmind.dashboard;

import com.example.metricmind.dto.analytics.DashboardDto;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.metricmind.ai.AiService;
import com.example.metricmind.dto.analytics.DashboardDto.Metrics;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {
    
        private final AiService aiService;

    public DashboardDto getDashboardData(String period) {

        log.info("TODO: fetch propertyId by user that made the request (he must select one in previous step (where we connect to GA4 admin api and get property))");

        if (period == null || (!period.equals("7") && !period.equals("30"))) {
            log.warn("Invalid period parameter: {}. Defaulting to 7 days.", period);
            period = "7";
        }

        Metrics metrics = Metrics.builder()
                .users(1200)
                .sessions(3000)
                .eventCount(15000)
                .conversions(250)
                .engagementTime(540000)
                .trafficSource(Map.of(
                        "organic", 1200,
                        "referral", 800,
                        "paid", 1000
                ))
                .build();
       
        var aiResponse = aiService.generateDashboardReport(
                com.example.metricmind.dto.ai.AiRequest.builder()
                        .period(period)
                        .metrics(Map.of(
                                "users", metrics.getUsers(),
                                "sessions", metrics.getSessions(),
                                "eventCount", metrics.getEventCount(),
                                "conversions", metrics.getConversions(),
                                "engagementTime", metrics.getEngagementTime(),
                                "trafficSource", metrics.getTrafficSource()
                        ))
                        .build()
        );

        return DashboardDto.builder()
                .metrics(metrics)
                .aiReport(
                        DashboardDto.AiReport.builder()
                                .summary(aiResponse.getSummary())
                                .explanation(aiResponse.getExplanation())
                                .recommendation(aiResponse.getRecommendation())
                                .build()
                )
                .build();
    }
}
