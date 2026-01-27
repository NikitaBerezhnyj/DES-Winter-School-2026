package com.example.metricmind.auth;

import com.example.metricmind.dto.auth.CurrentUserResponse;
import com.example.metricmind.dto.auth.LoginResponse;
import com.example.metricmind.exception.ApiException;
import com.example.metricmind.session.Session;
import com.example.metricmind.session.SessionService;
import com.example.metricmind.user.User;
import com.example.metricmind.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserService userService;
    private final SessionService sessionService;
    
    @Transactional
    public LoginResponse login(String idToken, String userAgent, String ipAddress) {
        log.info("Processing login request");
        
        GoogleUserInfo googleUserInfo = googleTokenVerifier.verifyToken(idToken);
        
        User user = userService.findOrCreateUser(googleUserInfo);
       
        Session session = sessionService.createSession(user, userAgent, ipAddress);
        
        log.info("User logged in successfully: {}", user.getEmail());
       
        return LoginResponse.builder()
                .message("Login successful")
                .user(buildUserInfo(user))
                .expiresAt(session.getExpiresAt())
                .sessionToken(session.getSessionToken())
                .build();
    }
    
    @Transactional
    public void logout(String sessionToken) {
        log.info("Processing logout request");
        sessionService.deactivateSession(sessionToken);
    }
    
    public CurrentUserResponse getCurrentUser(String sessionToken) {
        Session session = sessionService.findActiveSession(sessionToken);
        
        sessionService.updateLastAccessed(session);
        
        User user = session.getUser();
        
        return CurrentUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .pictureUrl(user.getPictureUrl())
                .hasGa4Access(userService.hasGa4Access(user))
                .selectedPropertyId(user.getSelectedPropertyId())
                .build();
    }
    
    public User getUserBySession(String sessionToken) {
        Session session = sessionService.findActiveSession(sessionToken);
        sessionService.updateLastAccessed(session);
        return session.getUser();
    }
    
    public boolean hasActiveSession(String sessionToken) {
        try {
            sessionService.findActiveSession(sessionToken);
            return true;
        } catch (ApiException e) {
            return false;
        }
    }
   
    private LoginResponse.UserInfo buildUserInfo(User user) {
        return LoginResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .pictureUrl(user.getPictureUrl())
                .hasGa4Access(userService.hasGa4Access(user))
                .selectedPropertyId(user.getSelectedPropertyId())
                .build();
    }
}