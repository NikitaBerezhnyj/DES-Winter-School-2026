package com.example.metricmind.analytics;

import com.example.metricmind.analytics.dto.Ga4PropertyDto;
import com.example.metricmind.exception.ApiException;
import com.example.metricmind.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Ga4AdminService {

    private final WebClient.Builder webClientBuilder;
    private final Ga4TokenService tokenService;
    private final ObjectMapper objectMapper;

    private static final String ADMIN_API_BASE = "https://analyticsadmin.googleapis.com/v1beta";
    
    public List<Ga4PropertyDto> listProperties(User user) {
        String accessToken = tokenService.getValidAccessToken(user);
        log.info("Fetching GA4 properties for user: {}", user.getEmail());

        try {
            String accountsJson = webClientBuilder.build()
                    .get()
                    .uri(ADMIN_API_BASE + "/accounts")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode accountsNode = objectMapper.readTree(accountsJson);
            List<Ga4PropertyDto> allProperties = new ArrayList<>();

            if (!accountsNode.has("accounts")) {
                log.warn("No GA4 accounts found for user: {}", user.getEmail());
                return allProperties;
            }
            
            for (JsonNode account : accountsNode.get("accounts")) {
                String accountName = account.get("name").asText();

                try {
                    String propertiesJson = webClientBuilder.build()
                            .get()
                            .uri(uriBuilder -> uriBuilder
                                    .scheme("https")
                                    .host("analyticsadmin.googleapis.com")
                                    .path("/v1beta/properties")
                                    .queryParam("filter", "parent:" + accountName)
                                    .build())
                            .header("Authorization", "Bearer " + accessToken)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

                    JsonNode propertiesNode = objectMapper.readTree(propertiesJson);

                    if (propertiesNode.has("properties")) {
                        for (JsonNode property : propertiesNode.get("properties")) {
                            allProperties.add(parseProperty(property));
                        }
                    }

                } catch (WebClientResponseException e) {
                    log.warn("Failed to fetch properties for account {}: {} {}",
                            accountName, e.getStatusCode(), e.getResponseBodyAsString());
                }
            }

            log.info("Found {} GA4 properties for user: {}", allProperties.size(), user.getEmail());
            return allProperties;

        } catch (WebClientResponseException e) {
            log.error("GA4 Admin API error: {} — {}", e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 401) {
                throw ApiException.unauthorized("GA4 access token is invalid or expired");
            }
            throw ApiException.badRequest("Failed to fetch GA4 accounts: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to fetch GA4 properties: {}", e.getMessage());
            throw ApiException.badRequest("Failed to fetch GA4 properties: " + e.getMessage());
        }
    }
    
    public Ga4PropertyDto getProperty(User user, String propertyId) {
        String accessToken = tokenService.getValidAccessToken(user);
        log.info("Fetching GA4 property {} for user: {}", propertyId, user.getEmail());

        try {
            String response = webClientBuilder.build()
                    .get()
                    .uri(ADMIN_API_BASE + "/properties/" + propertyId)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode propertyNode = objectMapper.readTree(response);
            return parseProperty(propertyNode);

        } catch (WebClientResponseException e) {
            log.error("GA4 Admin API error for property {}: {} — {}",
                    propertyId, e.getStatusCode(), e.getResponseBodyAsString());
            if (e.getStatusCode().value() == 404) {
                throw ApiException.badRequest("GA4 property not found: " + propertyId);
            }
            throw ApiException.badRequest("Failed to fetch GA4 property: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to fetch GA4 property: {}", e.getMessage());
            throw ApiException.badRequest("Failed to fetch GA4 property: " + e.getMessage());
        }
    }

    private Ga4PropertyDto parseProperty(JsonNode propertyNode) {
        String name = propertyNode.get("name").asText();
        String propertyId = name.substring(name.lastIndexOf("/") + 1);

        return Ga4PropertyDto.builder()
                .propertyId(propertyId)
                .displayName(propertyNode.path("displayName").asText(""))
                .industryCategory(propertyNode.path("industryCategory").asText(""))
                .timeZone(propertyNode.path("timeZone").asText(""))
                .currencyCode(propertyNode.path("currencyCode").asText(""))
                .build();
    }
}