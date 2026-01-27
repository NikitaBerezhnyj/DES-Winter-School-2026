package com.example.metricmind.session;

import com.example.metricmind.exception.ApiException;
import com.example.metricmind.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {
    
    private final SessionRepository sessionRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    
    @Value("${session.duration-hours:720}")
    private int sessionDurationHours;
    
    @Transactional
    public Session createSession(User user, String userAgent, String ipAddress) {
        String sessionToken = generateSecureToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(sessionDurationHours);
        
        Session session = Session.builder()
                .sessionToken(sessionToken)
                .user(user)
                .expiresAt(expiresAt)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .isActive(true)
                .build();
        
        Session savedSession = sessionRepository.save(session);
        log.info("Created new session for user: {} (expires at: {})", 
                 user.getEmail(), expiresAt);
        
        return savedSession;
    }
    
    public Session findActiveSession(String sessionToken) {
        return sessionRepository.findActiveByToken(sessionToken, LocalDateTime.now())
                .orElseThrow(() -> ApiException.unauthorized("Invalid or expired session"));
    }
    
    @Transactional
    public void updateLastAccessed(Session session) {
        session.setLastAccessedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
    
    @Transactional
    public void deactivateSession(String sessionToken) {
        sessionRepository.findActiveByToken(sessionToken, LocalDateTime.now())
                .ifPresent(session -> {
                    session.setActive(false);
                    sessionRepository.save(session);
                    log.info("Deactivated session for user: {}", session.getUser().getEmail());
                });
    }
    
    @Transactional
    public void deactivateAllUserSessions(User user) {
        sessionRepository.deactivateAllUserSessions(user);
        log.info("Deactivated all sessions for user: {}", user.getEmail());
    }
    
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        sessionRepository.deleteExpiredSessions(now);
        log.info("Cleaned up expired sessions at: {}", now);
    }
    
    private String generateSecureToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
    
    public boolean isSessionValid(Session session) {
        return session.isActive() && !session.isExpired();
    }
}