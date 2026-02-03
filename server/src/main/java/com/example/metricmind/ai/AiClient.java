package com.example.metricmind.ai;

import com.example.metricmind.config.AiConfig;
import com.example.metricmind.dto.ai.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AiClient {

    private final AiConfig properties;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper mapper;

    public AiResponse generate(String prompt) {

        OllamaResponse response = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build()
                .post()
                .uri("/api/generate")
                .bodyValue(Map.of(
                        "model", properties.getModel(),
                        "prompt", prompt,
                        "stream", false
                ))
                .retrieve()
                .bodyToMono(OllamaResponse.class)
                .block();

        return parse(response);
    }

    private AiResponse parse(OllamaResponse response) {
        try {
            return mapper.readValue(response.response(), AiResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid AI response", e);
        }
    }

    record OllamaResponse(String response) {}
}

