package com.example.metricmind.dashboard;

import com.example.metricmind.user.User;
import com.example.metricmind.ai.AiService;
import com.example.metricmind.ai.dto.AiRequest;
import com.example.metricmind.exception.ApiException;
import com.example.metricmind.dashboard.dto.DashboardDto;
import com.example.metricmind.analytics.dto.Ga4MetricsResponse;
import com.example.metricmind.analytics.services.Ga4DataService;
import com.example.metricmind.dashboard.dto.DashboardDto.Metrics;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

        private final AiService aiService;
        private final Ga4DataService ga4DataService;

        public DashboardDto getDashboardData(String period, User user) {
                if (user.getGa4AccessToken() == null) {
                        throw ApiException.badRequest("GA4 account not connected");
                }

                if (user.getSelectedPropertyId() == null) {
                        throw ApiException.badRequest("No GA4 property selected");
                }

                String validPeriod = (period != null && (period.equals("7") || period.equals("30")))
                                ? period
                                : "7";

                String startDate = validPeriod.equals("30") ? "30daysAgo" : "7daysAgo";
                String endDate = "today";

                log.info("Fetching dashboard data for user: {}, property: {}, period: {} days",
                                user.getEmail(), user.getSelectedPropertyId(), validPeriod);

                Ga4MetricsResponse ga4Data = ga4DataService.getMetrics(
                                user,
                                user.getSelectedPropertyId(),
                                startDate,
                                endDate);

                Map<String, Object> rawMetrics = ga4Data.getMetrics();
                @SuppressWarnings("unchecked")
                Map<String, Integer> trafficSource = (Map<String, Integer>) ga4Data.getDimensions()
                                .getOrDefault("trafficSource", Map.of());

                Metrics metrics = Metrics.builder()
                                .users(getInt(rawMetrics, "users"))
                                .sessions(getInt(rawMetrics, "sessions"))
                                .eventCount(getInt(rawMetrics, "eventCount"))
                                .conversions(getInt(rawMetrics, "conversions"))
                                .engagementTime(getDouble(rawMetrics, "engagementTime"))
                                .trafficSource(trafficSource)
                                .build();

                boolean hasData = metrics.getUsers() > 0 || metrics.getSessions() > 0;

                if (!hasData) {
                        return DashboardDto.builder()
                                        .metrics(metrics)
                                        .aiReport(DashboardDto.AiReport.builder()
                                                        .summary("Недостатньо даних для аналізу")
                                                        .explanation("За вибраний період не зафіксовано жодної активності.")
                                                        .recommendation("Переконайтесь що GA4 тег встановлено на сайті і збирає дані.")
                                                        .build())
                                        .build();
                }

                var aiResponse = aiService.generateDashboardReport(
                                AiRequest.builder()
                                                .period(validPeriod)
                                                .metrics(Map.of(
                                                                "users", metrics.getUsers(),
                                                                "sessions", metrics.getSessions(),
                                                                "eventCount", metrics.getEventCount(),
                                                                "conversions", metrics.getConversions(),
                                                                "engagementTime", metrics.getEngagementTime(),
                                                                "trafficSource", metrics.getTrafficSource()))
                                                .build());

                log.info("Dashboard data ready for user: {}", user.getEmail());

                return DashboardDto.builder()
                                .metrics(metrics)
                                .aiReport(DashboardDto.AiReport.builder()
                                                .summary(aiResponse.getSummary())
                                                .explanation(aiResponse.getExplanation())
                                                .recommendation(aiResponse.getRecommendation())
                                                .build())
                                .build();
        }

        private int getInt(Map<String, Object> map, String key) {
                Object val = map.get(key);
                if (val instanceof Integer i)
                        return i;
                if (val instanceof Number n)
                        return n.intValue();
                return 0;
        }

        private double getDouble(Map<String, Object> map, String key) {
                Object val = map.get(key);
                if (val instanceof Double d)
                        return d;
                if (val instanceof Number n)
                        return n.doubleValue();
                return 0.0;
        }
}