package com.example.metricmind.dashboard;

import com.example.metricmind.dto.analytics.DashboardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DashboardService {
    
    public DashboardDto getDashboardData(String period) {
        log.info("TODO: fetch propertyId by user that made the request (he must select one in previous step (where we connect to GA4 admin api and get property))");

        if (period == null || (!period.equals("7") && !period.equals("30"))) {
            log.warn("Invalid period parameter: {}. Defaulting to 7 days.", period);
            period = "7";
        }
       
        DashboardDto.Metrics metrics = DashboardDto.Metrics.builder()
                .users(234)
                .sessions(567)
                .eventCount(890)
                .conversions(12)
                .engagementTime(345.5)
                .trafficSource(Map.of(
                        "organic", 120,
                        "paid", 50,
                        "referral", 30
                ))
                .build();
        
        DashboardDto.AiReport aiReport = DashboardDto.AiReport.builder()
                .summary("Traffic is stable with slight growth in sessions over the selected period.")
                .explanation("Users increased by 5% compared to the previous period.")
                .recommendation("Focus on improving engagement on top landing pages.")
                .build();

        return DashboardDto.builder()
                .metrics(metrics)
                .aiReport(aiReport)
                .build();
    }
}
