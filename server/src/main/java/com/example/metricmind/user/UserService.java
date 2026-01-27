package com.example.metricmind.user;

import com.example.metricmind.auth.GoogleUserInfo;
import com.example.metricmind.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public User findOrCreateUser(GoogleUserInfo googleUserInfo) {
        return userRepository.findByGoogleUserId(googleUserInfo.getGoogleUserId())
                .map(existingUser -> {
                    log.info("Found existing user: {}", existingUser.getEmail());
                    return updateUserInfo(existingUser, googleUserInfo);
                })
                .orElseGet(() -> {
                    log.info("Creating new user: {}", googleUserInfo.getEmail());
                    return createNewUser(googleUserInfo);
                });
    }
   
    private User updateUserInfo(User user, GoogleUserInfo googleUserInfo) {
        boolean updated = false;
        
        if (!user.getEmail().equals(googleUserInfo.getEmail())) {
            user.setEmail(googleUserInfo.getEmail());
            updated = true;
        }
        
        if (googleUserInfo.getName() != null && !googleUserInfo.getName().equals(user.getName())) {
            user.setName(googleUserInfo.getName());
            updated = true;
        }
        
        if (googleUserInfo.getPictureUrl() != null && !googleUserInfo.getPictureUrl().equals(user.getPictureUrl())) {
            user.setPictureUrl(googleUserInfo.getPictureUrl());
            updated = true;
        }
        
        if (updated) {
            log.info("Updated user info for: {}", user.getEmail());
            return userRepository.save(user);
        }
        
        return user;
    }
    
    private User createNewUser(GoogleUserInfo googleUserInfo) {
        User newUser = User.builder()
                .googleUserId(googleUserInfo.getGoogleUserId())
                .email(googleUserInfo.getEmail())
                .name(googleUserInfo.getName())
                .pictureUrl(googleUserInfo.getPictureUrl())
                .build();
        
        User savedUser = userRepository.save(newUser);
        log.info("Created new user with ID: {}", savedUser.getId());
        
        return savedUser;
    }
    
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));
    }
    
    @Transactional
    public void updateGa4Tokens(User user, String accessToken, String refreshToken, LocalDateTime expiresAt) {
        user.setGa4AccessToken(accessToken);
        user.setGa4RefreshToken(refreshToken);
        user.setGa4TokenExpiresAt(expiresAt);
        userRepository.save(user);
        
        log.info("Updated GA4 tokens for user: {}", user.getEmail());
    }
    
    @Transactional
    public void updateSelectedProperty(User user, String propertyId) {
        user.setSelectedPropertyId(propertyId);
        userRepository.save(user);
        
        log.info("Updated selected property for user {}: {}", user.getEmail(), propertyId);
    }
    
    public boolean hasGa4Access(User user) {
        return user.getGa4AccessToken() != null 
                && user.getGa4RefreshToken() != null
                && user.getGa4TokenExpiresAt() != null;
    }
}