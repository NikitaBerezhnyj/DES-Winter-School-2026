package com.example.metricmind.analytics;

import com.example.metricmind.exception.ApiException;
import com.example.metricmind.user.User;
import com.example.metricmind.user.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Ga4TokenService {
    
    private final WebClient.Builder webClientBuilder;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    
    @Value("${google.client-id}")
    private String clientId;
    
    @Value("${google.client-secret}")
    private String clientSecret;
    
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://localhost:8080/api/analytics/oauth2/callback";
    
    public void exchangeCodeForTokens(User user, String authorizationCode) {
        log.info("Exchanging authorization code for tokens for user: {}", user.getEmail());
        
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("code", authorizationCode);
        tokenRequest.put("client_id", clientId);
        tokenRequest.put("client_secret", clientSecret);
        tokenRequest.put("redirect_uri", REDIRECT_URI);
        tokenRequest.put("grant_type", "authorization_code");
        
        try {
            String response = webClientBuilder.build()
                    .post()
                    .uri(TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("code", authorizationCode)
                            .with("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("redirect_uri", REDIRECT_URI)
                            .with("grant_type", "authorization_code"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            JsonNode jsonNode = objectMapper.readTree(response);
            
            String accessToken = jsonNode.get("access_token").asText();
            String refreshToken = jsonNode.has("refresh_token") ? 
                    jsonNode.get("refresh_token").asText() : null;
            int expiresIn = jsonNode.get("expires_in").asInt();
            
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expiresIn);
            
            userService.updateGa4Tokens(user, accessToken, refreshToken, expiresAt);
            
            log.info("Successfully exchanged code for tokens for user: {}", user.getEmail());
            
        } catch (Exception e) {
            log.error("Failed to exchange authorization code: {}", e.getMessage());
            throw ApiException.badRequest("Failed to exchange authorization code: " + e.getMessage());
        }
    }
    
    public String refreshAccessToken(User user) {
        if (user.getGa4RefreshToken() == null) {
            throw ApiException.unauthorized("No refresh token available for user"); 
        }
        
        log.info("Refreshing access token for user: {}", user.getEmail());
        
        try {
            String response = webClientBuilder.build()
                    .post()
                    .uri(TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("refresh_token", user.getGa4RefreshToken())
                            .with("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("grant_type", "refresh_token"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            JsonNode jsonNode = objectMapper.readTree(response);
            
            String accessToken = jsonNode.get("access_token").asText();
            int expiresIn = jsonNode.get("expires_in").asInt();
            
            LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expiresIn);
            
            userService.updateGa4Tokens(user, accessToken, user.getGa4RefreshToken(), expiresAt);
            
            log.info("Successfully refreshed access token for user: {}", user.getEmail());
            
            return accessToken;
            
        } catch (Exception e) {
            log.error("Failed to refresh access token: {}", e.getMessage());
            throw ApiException.unauthorized("Failed to refresh access token: " + e.getMessage());
        }
    }
    
    public String getValidAccessToken(User user) {
        if (user.getGa4AccessToken() == null) {
            throw ApiException.unauthorized("User has not connected GA4 account");
        }
        
        if (user.getGa4TokenExpiresAt() != null && 
            user.getGa4TokenExpiresAt().minusMinutes(5).isAfter(LocalDateTime.now())) {
            return user.getGa4AccessToken();
        }
        
        return refreshAccessToken(user);
    }
}