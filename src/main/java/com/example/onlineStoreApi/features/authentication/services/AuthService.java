package com.example.onlineStoreApi.features.authentication.services;

import com.example.onlineStoreApi.features.authentication.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {
    AuthResponse login(LoginDto loginDto);

    AuthResponse register(RegisterDto registerDto);

    RefreshTokenResponse refreshToken(RefreshDto refreshDto);

    void logout(String token, HttpServletRequest request, HttpServletResponse response);
}
