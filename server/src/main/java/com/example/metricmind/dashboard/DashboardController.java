package com.example.metricmind.dashboard;

import com.example.metricmind.auth.AuthService;
import com.example.metricmind.dto.analytics.DashboardDto;
import com.example.metricmind.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
            HttpServletRequest request
    ) {
        User user = getUserFromRequest(request);
        log.info("Dashboard request for period: {}, user: {}", period, user.getEmail());

        DashboardDto dashboardData = dashboardService.getDashboardData(period, user);
        return ResponseEntity.ok(dashboardData);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new RuntimeException("Unauthorized");

        String sessionToken = Arrays.stream(cookies)
                .filter(c -> sessionCookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        return authService.getUserBySession(sessionToken);
    }
}