package com.example.metricmind.analytics.services;

import com.example.metricmind.analytics.dto.Ga4MetricsResponse;
import com.example.metricmind.exception.ApiException;
import com.example.metricmind.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Ga4DataService {

    private final WebClient.Builder webClientBuilder;
    private final Ga4TokenService tokenService;
    private final ObjectMapper objectMapper;

    private static final String DATA_API_BASE = "https://analyticsdata.googleapis.com/v1beta";

    public Ga4MetricsResponse getMetrics(User user, String propertyId, String startDate, String endDate) {
        String accessToken = tokenService.getValidAccessToken(user);
        log.info("Fetching GA4 metrics for property {} [{} — {}], user: {}",
                propertyId, startDate, endDate, user.getEmail());

        Map<String, Object> requestBody = buildMetricsRequest(startDate, endDate);

        try {
            String response = webClientBuilder.build()
                    .post()
                    .uri(DATA_API_BASE + "/properties/" + propertyId + ":runReport")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode responseNode = objectMapper.readTree(response);
            return parseMetricsResponse(propertyId, startDate, endDate, responseNode);

        } catch (WebClientResponseException e) {
            log.error("GA4 Data API error for property {}: {} — {}",
                    propertyId, e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 403) {
                throw ApiException.unauthorized("No access to GA4 property: " + propertyId);
            }
            throw ApiException.badRequest("Failed to fetch GA4 metrics: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to fetch GA4 metrics: {}", e.getMessage());
            throw ApiException.badRequest("Failed to fetch GA4 metrics: " + e.getMessage());
        }
    }

    private Map<String, Object> buildMetricsRequest(String startDate, String endDate) {
        Map<String, Object> request = new HashMap<>();

        Map<String, String> dateRange = new HashMap<>();
        dateRange.put("startDate", startDate);
        dateRange.put("endDate", endDate);
        request.put("dateRanges", new Object[] { dateRange });

        request.put("metrics", new Object[] {
                Map.of("name", "activeUsers"),
                Map.of("name", "sessions"),
                Map.of("name", "eventCount"),
                Map.of("name", "conversions"),
                Map.of("name", "userEngagementDuration")
        });

        request.put("dimensions", new Object[] {
                Map.of("name", "sessionDefaultChannelGroup")
        });

        request.put("metricAggregations", new Object[] { "TOTAL" });

        return request;
    }

    private Ga4MetricsResponse parseMetricsResponse(String propertyId, String startDate,
            String endDate, JsonNode responseNode) {
        Map<String, Object> metrics = new HashMap<>();
        Map<String, Object> dimensions = new HashMap<>();

        if (responseNode.has("totals") && responseNode.get("totals").size() > 0) {
            JsonNode totals = responseNode.get("totals").get(0);
            JsonNode metricValues = totals.get("metricValues");

            if (metricValues != null) {
                if (metricValues.size() > 0)
                    metrics.put("users",
                            safeInt(metricValues.get(0)));
                if (metricValues.size() > 1)
                    metrics.put("sessions",
                            safeInt(metricValues.get(1)));
                if (metricValues.size() > 2)
                    metrics.put("eventCount",
                            safeInt(metricValues.get(2)));
                if (metricValues.size() > 3)
                    metrics.put("conversions",
                            safeInt(metricValues.get(3)));
                if (metricValues.size() > 4)
                    metrics.put("engagementTime",
                            safeDouble(metricValues.get(4)));
            }
        } else {
            log.warn("No totals in GA4 response for property: {}", propertyId);
        }

        if (responseNode.has("rows")) {
            Map<String, Integer> trafficSource = new HashMap<>();

            for (JsonNode row : responseNode.get("rows")) {
                JsonNode dimValues = row.get("dimensionValues");
                JsonNode metValues = row.get("metricValues");

                if (dimValues != null && metValues != null
                        && dimValues.size() > 0 && metValues.size() > 0) {
                    String channelGroup = dimValues.get(0).path("value").asText("unknown");
                    int users = safeInt(metValues.get(0));
                    trafficSource.put(channelGroup, users);
                }
            }

            dimensions.put("trafficSource", trafficSource);
        }

        return Ga4MetricsResponse.builder()
                .propertyId(propertyId)
                .dateRange(startDate + " to " + endDate)
                .metrics(metrics)
                .dimensions(dimensions)
                .build();
    }

    private int safeInt(JsonNode node) {
        if (node == null)
            return 0;
        return node.path("value").asInt(0);
    }

    private double safeDouble(JsonNode node) {
        if (node == null)
            return 0.0;
        return node.path("value").asDouble(0.0);
    }
}