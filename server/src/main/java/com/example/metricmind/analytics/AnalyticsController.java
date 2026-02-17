package com.example.metricmind.analytics;

import com.example.metricmind.analytics.dto.Ga4AuthRequest;
import com.example.metricmind.analytics.dto.Ga4AuthResponse;
import com.example.metricmind.analytics.dto.Ga4MetricsRequest;
import com.example.metricmind.analytics.dto.Ga4MetricsResponse;
import com.example.metricmind.analytics.dto.Ga4PropertyDto;
import com.example.metricmind.auth.AuthService;
import com.example.metricmind.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final AuthService authService;

    @Value("${session.cookie-name:SESSION_TOKEN}")
    private String sessionCookieName;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    @GetMapping("/oauth2/authorize-url")
    public ResponseEntity<AuthorizeUrlResponse> getAuthorizeUrl(HttpServletRequest request) {
        String sessionToken = getSessionTokenFromCookie(request);
        if (sessionToken == null) {
            return ResponseEntity.status(401).build();
        }
        
        String loginHint = null;
        try {
            User user = authService.getUserBySession(sessionToken);
            loginHint = user.getEmail();
        } catch (Exception e) {
            log.warn("Could not get user email for login hint: {}", e.getMessage());
        }

        String redirectUri = backendUrl + "/api/analytics/oauth2/callback";

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/analytics.readonly")
                .queryParam("access_type", "offline")
                .queryParam("include_granted_scopes", "true")
                .queryParam("prompt", "consent select_account")
                .queryParam("state", sessionToken);

        if (loginHint != null) {
            builder.queryParam("login_hint", loginHint);
        }

        String authUrl = builder.build(false).encode().toUriString();
        log.info("Generated GA4 authorize URL for user: {}", loginHint);

        return ResponseEntity.ok(new AuthorizeUrlResponse(authUrl));
    }
    
    @GetMapping("/oauth2/callback")
    public ResponseEntity<Void> handleOAuth2Callback(
            @RequestParam String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            HttpServletRequest request
    ) {
        if (error != null) {
            log.warn("GA4 OAuth error: {}", error);
            return ResponseEntity.status(302)
                    .header("Location", frontendUrl + "/dashboard?ga4=error&reason=" + error)
                    .build();
        }
        
        String sessionToken = (state != null && !state.isBlank())
                ? state
                : getSessionTokenFromCookie(request);

        if (sessionToken == null) {
            log.error("No session token in state or cookie during GA4 callback");
            return ResponseEntity.status(302)
                    .header("Location", frontendUrl + "/login?error=session_expired")
                    .build();
        }

        try {
            User user = authService.getUserBySession(sessionToken);
            log.info("Processing GA4 callback for user: {}", user.getEmail());

            analyticsService.connectGa4Account(user, code);
            
            return ResponseEntity.status(302)
                    .header("Location", frontendUrl + "/dashboard?ga4=success")
                    .build();

        } catch (Exception e) {
            log.error("Failed to connect GA4 for session {}: {}", sessionToken, e.getMessage());
            return ResponseEntity.status(302)
                    .header("Location", frontendUrl + "/dashboard?ga4=error&reason=token_exchange_failed")
                    .build();
        }
    }
    
    @GetMapping("/properties")
    public ResponseEntity<List<Ga4PropertyDto>> getProperties(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        List<Ga4PropertyDto> properties = analyticsService.getUserProperties(user);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/properties/{propertyId}")
    public ResponseEntity<Ga4PropertyDto> getProperty(
            @PathVariable String propertyId,
            HttpServletRequest request
    ) {
        User user = getUserFromRequest(request);
        Ga4PropertyDto property = analyticsService.getProperty(user, propertyId);
        return ResponseEntity.ok(property);
    }

    @PostMapping("/properties/{propertyId}/select")
    public ResponseEntity<Void> selectProperty(
            @PathVariable String propertyId,
            HttpServletRequest request
    ) {
        User user = getUserFromRequest(request);
        analyticsService.selectProperty(user, propertyId);
        log.info("User {} selected property: {}", user.getEmail(), propertyId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/metrics")
    public ResponseEntity<Ga4MetricsResponse> getMetrics(
            @Valid @RequestBody Ga4MetricsRequest metricsRequest,
            HttpServletRequest httpRequest
    ) {
        User user = getUserFromRequest(httpRequest);
        Ga4MetricsResponse metrics = analyticsService.getMetrics(
                user,
                metricsRequest.getPropertyId(),
                metricsRequest.getStartDate(),
                metricsRequest.getEndDate()
        );
        return ResponseEntity.ok(metrics);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        String sessionToken = getSessionTokenFromCookie(request);
        if (sessionToken == null) {
            throw new RuntimeException("Unauthorized");
        }
        return authService.getUserBySession(sessionToken);
    }

    private String getSessionTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(cookie -> sessionCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public record AuthorizeUrlResponse(String url) {}
}