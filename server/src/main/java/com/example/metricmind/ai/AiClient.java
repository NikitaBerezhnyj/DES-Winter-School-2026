package com.example.metricmind.ai;

import com.example.metricmind.config.AiConfig;
import com.example.metricmind.dto.ai.AiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiClient {
    
    private final AiConfig properties;
    private final WebClient aiWebClient;
    private final ObjectMapper mapper;
    private final PromptBuilder promptBuilder;

    public void ping() {
        log.debug("Pinging AI service to warm up model...");
        
        Map<String, Object> requestBody = Map.of(
            "model", properties.getModel(),
            "messages", List.of(
                Map.of("role", "user", "content", "Hi")
            ),
            "stream", false,
            "options", Map.of(
                "temperature", 0.1,
                "num_predict", 5
            )
        );
        
        aiWebClient
            .post()
            .uri("/api/chat")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(OllamaChatResponse.class)
            .timeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
            .block();
        
        log.debug("AI ping completed");
    }
    
    public AiResponse generate(String userPrompt) {
        log.debug("Sending request to AI service: {}", properties.getBaseUrl());
        
        Map<String, Object> requestBody = Map.of(
            "model", properties.getModel(),
            "messages", List.of(
                Map.of("role", "system", "content", promptBuilder.getSystemPrompt()),
                Map.of("role", "user", "content", userPrompt)
            ),
            "format", "json",
            "stream", false,
            "options", Map.of(
                "temperature", 0.3,
                "num_predict", 500
            )
        );
        
        OllamaChatResponse response = aiWebClient
                .post()
                .uri("/api/chat")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OllamaChatResponse.class)
                .timeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                .block();
        
        if (response == null || response.message() == null) {
            throw new RuntimeException("Empty response from AI service");
        }
        
        return parse(response.message().content());
    }
    
    private AiResponse parse(String jsonContent) {
        try {
            log.debug("Parsing AI response: {}", jsonContent);
            return mapper.readValue(jsonContent, AiResponse.class);
        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", jsonContent, e);
            throw new RuntimeException("Invalid AI response format", e);
        }
    }
    
    record OllamaChatResponse(
        String model,
        Message message,
        boolean done
    ) {}
    
    record Message(
        String role,
        String content
    ) {}
}