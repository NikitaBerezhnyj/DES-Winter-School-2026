package com.example.metricmind.ai;

import com.example.metricmind.dto.ai.AiRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PromptBuilder {
    
    private final ObjectMapper mapper;
    
    private static final String SYSTEM_PROMPT = """
        You are a professional Google Analytics consultant. 
        Analyze metrics and provide actionable insights in JSON format with three fields:
        - 'summary': brief overview (2-3 sentences max)
        - 'explanation': detailed analysis of key changes and trends
        - 'recommendation': specific actionable advice for improvement
        """;
    
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
            
            metricsText.append("\nProvide analysis in JSON format with fields: summary, explanation, recommendation.");
            
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