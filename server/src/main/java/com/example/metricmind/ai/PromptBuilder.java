package com.example.metricmind.ai;

import com.example.metricmind.dto.ai.AiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptBuilder {

    private final ObjectMapper mapper;

    public String build(AiRequest request) {
        try {
            return """
                You are a product analytics assistant.
                Analyze the following aggregated GA4 metrics and return:
                - summary
                - explanation
                - recommendation

                Period: %s days

                Metrics:
                %s

                Respond in JSON with fields:
                summary, explanation, recommendation
                """.formatted(
                    request.getPeriod(),
                    mapper.writeValueAsString(request.getMetrics())
                );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build AI prompt", e);
        }
    }
}

