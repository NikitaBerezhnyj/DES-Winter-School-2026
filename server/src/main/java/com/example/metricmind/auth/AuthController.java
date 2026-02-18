package com.example.metricmind.auth;

import com.example.metricmind.utils.CookieUtils;
import com.example.metricmind.auth.dto.LoginRequest;
import com.example.metricmind.auth.dto.LoginResponse;
import com.example.metricmind.auth.dto.CurrentUserResponse;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;

        @Value("${session.cookie-name:SESSION_TOKEN}")
        private String sessionCookieName;

        @Value("${session.cookie-secure:true}")
        private boolean cookieSecure;

        @Value("${session.cookie-same-site:Strict}")
        private String cookieSameSite;

        @Value("${session.duration-hours:720}")
        private int sessionDurationHours;

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                        @Valid @RequestBody LoginRequest request,
                        HttpServletRequest httpRequest,
                        HttpServletResponse httpResponse) {

                String userAgent = httpRequest.getHeader("User-Agent");
                String ipAddress = getClientIpAddress(httpRequest);

                log.info("Login request from IP: {}", ipAddress);

                LoginResponse response = authService.login(
                                request.getIdToken(),
                                userAgent,
                                ipAddress);

                CookieUtils.setSessionCookie(
                                httpResponse,
                                sessionCookieName,
                                response.getSessionToken(),
                                sessionDurationHours * 3600,
                                cookieSecure,
                                cookieSameSite);

                return ResponseEntity.ok(response);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> logout(
                        HttpServletRequest request,
                        HttpServletResponse response) {

                String sessionToken = CookieUtils.getCookieValue(request, sessionCookieName);

                if (sessionToken != null) {
                        authService.logout(sessionToken);
                }

                CookieUtils.clearCookie(response, sessionCookieName, cookieSecure);

                log.info("User logged out successfully");

                return ResponseEntity.ok().build();
        }

        @GetMapping("/me")
        public ResponseEntity<CurrentUserResponse> getCurrentUser(
                        HttpServletRequest request) {

                String sessionToken = CookieUtils.getCookieValue(request, sessionCookieName);

                if (sessionToken == null) {
                        return ResponseEntity.status(401).build();
                }

                CurrentUserResponse user = authService.getCurrentUser(sessionToken);

                return ResponseEntity.ok(user);
        }

        @GetMapping("/check")
        public ResponseEntity<CheckResponse> checkAuth(
                        HttpServletRequest request) {

                String sessionToken = CookieUtils.getCookieValue(request, sessionCookieName);

                boolean authenticated = sessionToken != null
                                && authService.hasActiveSession(sessionToken);

                return ResponseEntity.ok(new CheckResponse(authenticated));
        }

        private String getClientIpAddress(HttpServletRequest request) {

                String xForwardedFor = request.getHeader("X-Forwarded-For");

                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                        return xForwardedFor.split(",")[0].trim();
                }

                return request.getRemoteAddr();
        }

        public record CheckResponse(boolean authenticated) {
        }
}
