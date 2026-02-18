package com.example.metricmind.auth.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponse {

    private UUID id;
    private String email;
    private String name;
    private String pictureUrl;
    private boolean hasGa4Access;
    private String selectedPropertyId;
}