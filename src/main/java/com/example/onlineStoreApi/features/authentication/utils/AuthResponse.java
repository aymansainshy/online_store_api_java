package com.example.onlineStoreApi.features.authentication.utils;

import com.example.onlineStoreApi.features.users.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private User user;
    private String accessToken;
    private String refreshToken;
}
