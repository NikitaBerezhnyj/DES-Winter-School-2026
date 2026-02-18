package com.example.metricmind.ai;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.metricmind.ai.dto.AiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PromptBuilder {

    private final ObjectMapper mapper;

    private static final String RESPONSE_LANGUAGE = "Ukrainian";
    private static final String SYSTEM_PROMPT_TEMPLATE = """
            You are a Google Analytics consultant.

            You MUST respond with ONLY a raw JSON object.
            Do NOT wrap it in markdown.
            Do NOT add explanations before or after.
            Do NOT add code fences.
            Return pure JSON only.

            The JSON must contain exactly these three string fields:

            {
            "summary": "brief overview here",
            "explanation": "detailed analysis here as plain text",
            "recommendation": "actionable advice here as plain text"
            }

            All three values MUST be plain text strings.
            Do NOT use nested objects.
            Do NOT use arrays.
            All responses must be in %s.
            """;
    private static final String SYSTEM_PROMPT = String.format(SYSTEM_PROMPT_TEMPLATE, RESPONSE_LANGUAGE);

    public String build(AiRequest request) {
        try {
            StringBuilder metricsText = new StringBuilder();
            Map<String, Object> metrics = request.getMetrics();

            metricsText.append("Analyze the following GA4 metrics for the last ")
                    .append(request.getPeriod())
                    .append(" days:\n\n");

            metricsText.append("Metrics:\n");
            metrics.forEach((key, value) -> {
                metricsText.append("- ").append(formatMetricName(key))
                        .append(": ").append(formatMetricValue(value))
                        .append("\n");
            });

            metricsText.append("\nRespond with ONLY a JSON object. ")
                    .append("Values for summary, explanation, and recommendation ")
                    .append("must be plain text strings, not objects or arrays.");

            return metricsText.toString();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to build AI prompt", e);
        }
    }

    public String getSystemPrompt() {
        return SYSTEM_PROMPT;
    }

    private String formatMetricName(String key) {
        return key.replaceAll("([A-Z])", " $1")
                .substring(0, 1).toUpperCase() +
                key.replaceAll("([A-Z])", " $1").substring(1);
    }

    private String formatMetricValue(Object value) {
        if (value == null) {
            return "N/A";
        }

        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> valueMap = (Map<String, Object>) value;

            if (valueMap.containsKey("current")) {
                Object current = valueMap.get("current");
                Object change = valueMap.get("change");

                if (change != null) {
                    double changeValue = ((Number) change).doubleValue();
                    String changeSymbol = changeValue >= 0 ? "↑" : "↓";
                    return String.format("%s (%s%.1f%% vs previous period)",
                            current, changeSymbol, Math.abs(changeValue));
                }
                return current.toString();
            }

            try {
                return mapper.writeValueAsString(valueMap);
            } catch (Exception e) {
                return valueMap.toString();
            }
        }

        return value.toString();
    }
}