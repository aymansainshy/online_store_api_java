package com.example.onlineStoreApi.features.authentication.services;

import com.example.onlineStoreApi.features.authentication.utils.*;


public interface AuthService {
    AuthResponse login(LoginDto loginDto);

    AuthResponse register(RegisterDto registerDto);

    RefreshTokenResponse refreshToken(RefreshDto refreshDto);
}
