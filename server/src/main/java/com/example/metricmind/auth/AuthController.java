package com.example.metricmind.auth;

import com.example.metricmind.dto.auth.CurrentUserResponse;
import com.example.metricmind.dto.auth.LoginRequest;
import com.example.metricmind.dto.auth.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
            HttpServletResponse httpResponse
    ) {
        String userAgent = httpRequest.getHeader("User-Agent");
        String ipAddress = getClientIpAddress(httpRequest);
        
        log.info("Login request from IP: {}", ipAddress);
        
        LoginResponse response = authService.login(
                request.getIdToken(),
                userAgent,
                ipAddress
        );
        
        setSessionCookie(httpResponse, response);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String sessionToken = getSessionTokenFromCookie(request);
        
        if (sessionToken != null) {
            authService.logout(sessionToken);
        }
        
        clearSessionCookie(response);
        
        log.info("User logged out successfully");
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(HttpServletRequest request) {
        String sessionToken = getSessionTokenFromCookie(request);
        
        if (sessionToken == null) {
            return ResponseEntity.status(401).build();
        }
        
        CurrentUserResponse user = authService.getCurrentUser(sessionToken);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/check")
    public ResponseEntity<CheckResponse> checkAuth(HttpServletRequest request) {
        String sessionToken = getSessionTokenFromCookie(request);
        
        boolean authenticated = sessionToken != null 
                && authService.hasActiveSession(sessionToken);
        
        return ResponseEntity.ok(new CheckResponse(authenticated));
    }
    
    private void setSessionCookie(HttpServletResponse response, LoginResponse loginResponse) {
        String sessionToken = loginResponse.getSessionToken();
        
        String cookieValue = String.format(
                "%s=%s; Path=/; Max-Age=%d; HttpOnly; %s; SameSite=%s",
                sessionCookieName,
                sessionToken,
                sessionDurationHours * 3600,
                cookieSecure ? "Secure" : "",
                cookieSameSite
        );
        
        response.addHeader("Set-Cookie", cookieValue);
        
        log.debug("Session cookie set successfully");
    }
    
    private String getSessionTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies == null) {
            return null;
        }
        
        return Arrays.stream(cookies)
                .filter(cookie -> sessionCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
    
    private void clearSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(sessionCookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        
        response.addCookie(cookie);
    }
  
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        return request.getRemoteAddr();
    }
    
    public record CheckResponse(boolean authenticated) {}
}