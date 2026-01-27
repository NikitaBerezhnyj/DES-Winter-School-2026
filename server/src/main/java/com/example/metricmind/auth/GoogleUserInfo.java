package com.example.metricmind.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserInfo {

    private String googleUserId;
    private String email;
    private String name;
    private String pictureUrl;
    private boolean emailVerified;
}
