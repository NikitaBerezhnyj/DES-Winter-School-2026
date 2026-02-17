package com.example.metricmind.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiWarmup {

    private final AiClient aiClient;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void warmup() {
        log.info("Starting AI model warmup...");
        try {
            aiClient.ping();
            log.info("AI model warmup completed successfully");
        } catch (Exception e) {
            log.warn("AI model warmup failed (non-critical): {}", e.getMessage());
        }
    }
}