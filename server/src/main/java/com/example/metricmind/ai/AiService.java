package com.example.metricmind.ai;

import com.example.metricmind.dto.ai.AiRequest;
import com.example.metricmind.dto.ai.AiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
