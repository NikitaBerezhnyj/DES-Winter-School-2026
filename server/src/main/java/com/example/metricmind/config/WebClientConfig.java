package com.example.metricmind.config;

import com.example.metricmind.ai.AiProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient aiWebClient(AiProperties aiProperties) {
        return WebClient.builder()
                .baseUrl(aiProperties.getBaseUrl())
                .build();
    }
}
