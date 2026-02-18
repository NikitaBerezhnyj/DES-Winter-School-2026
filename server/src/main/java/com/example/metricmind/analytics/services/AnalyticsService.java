package com.example.metricmind.analytics.services;

import com.example.metricmind.analytics.dto.*;
import com.example.metricmind.user.User;
import com.example.metricmind.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final Ga4TokenService tokenService;
    private final Ga4AdminService adminService;
    private final Ga4DataService dataService;
    private final UserService userService;

    @Transactional
    public Ga4AuthResponse connectGa4Account(User user, String authorizationCode) {
        log.info("Connecting GA4 account for user: {}", user.getEmail());

        tokenService.exchangeCodeForTokens(user, authorizationCode);

        return Ga4AuthResponse.builder()
                .success(true)
                .message("Successfully connected GA4 account")
                .tokenExpiresAt(user.getGa4TokenExpiresAt())
                .build();
    }

    public List<Ga4PropertyDto> getUserProperties(User user) {
        return adminService.listProperties(user);
    }

    public Ga4PropertyDto getProperty(User user, String propertyId) {
        return adminService.getProperty(user, propertyId);
    }

    @Transactional
    public void selectProperty(User user, String propertyId) {
        adminService.getProperty(user, propertyId);

        userService.updateSelectedProperty(user, propertyId);

        log.info("User {} selected property: {}", user.getEmail(), propertyId);
    }

    public Ga4MetricsResponse getMetrics(User user, String propertyId, String startDate, String endDate) {
        return dataService.getMetrics(user, propertyId, startDate, endDate);
    }
}