package com.example.metricmind.ai;

import com.example.metricmind.ai.dto.AiRequest;
import com.example.metricmind.ai.dto.AiResponse;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final AiClient aiClient;
    private final PromptBuilder promptBuilder;

    public AiResponse generateDashboardReport(AiRequest request) {
        log.info("Generating AI report for period {} days", request.getPeriod());

        String prompt = promptBuilder.build(request);

        return aiClient.generate(prompt);
    }
}
