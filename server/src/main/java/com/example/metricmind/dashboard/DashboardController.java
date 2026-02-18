package com.example.metricmind.dashboard;

import com.example.metricmind.user.User;
import com.example.metricmind.auth.AuthService;
import com.example.metricmind.utils.CookieUtils;
import com.example.metricmind.dashboard.dto.DashboardDto;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthService authService;

    @Value("${session.cookie-name:SESSION_TOKEN}")
    private String sessionCookieName;

    @GetMapping
    public ResponseEntity<DashboardDto> getDashboard(
            @RequestParam(defaultValue = "7") String period,
            HttpServletRequest request) {
        User user = getUserFromRequest(request);

        log.info("Dashboard request for period: {}, user: {}",
                period,
                user.getEmail());

        DashboardDto dashboardData = dashboardService.getDashboardData(period, user);

        return ResponseEntity.ok(dashboardData);
    }

    private User getUserFromRequest(HttpServletRequest request) {

        String sessionToken = CookieUtils.getCookieValue(request, sessionCookieName);

        if (sessionToken == null) {
            throw new RuntimeException("Unauthorized");
        }

        return authService.getUserBySession(sessionToken);
    }
}
