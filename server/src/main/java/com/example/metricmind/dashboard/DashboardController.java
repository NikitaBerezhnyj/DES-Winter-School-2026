package com.example.metricmind.dashboard;

import com.example.metricmind.dto.analytics.DashboardDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    
    @GetMapping
    public ResponseEntity<DashboardDto> getDashboard(
            @RequestParam(defaultValue = "7") String period,
            HttpServletRequest request
    ) {
        log.info("Dashboard request for period: {}", period);

        DashboardDto dashboardData = dashboardService.getDashboardData(period);

        return ResponseEntity.ok(dashboardData);
    }
}
