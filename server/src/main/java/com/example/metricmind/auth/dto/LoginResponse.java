package com.example.metricmind.auth.dto;

import lombok.*;

import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String message;
    private UserInfo user;
    private LocalDateTime expiresAt;
    private String sessionToken;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private UUID id;
        private String email;
        private String name;
        private String pictureUrl;
        private boolean hasGa4Access;
        private String selectedPropertyId;
    }
}
