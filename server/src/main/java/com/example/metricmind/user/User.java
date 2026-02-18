package com.example.metricmind.user;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "google_user_id", unique = true, nullable = false)
    private String googleUserId;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    @Column(name = "picture_url")
    private String pictureUrl;

    private String ga4AccessToken;
    private String ga4RefreshToken;
    private LocalDateTime ga4TokenExpiresAt;

    private String selectedPropertyId;
}
