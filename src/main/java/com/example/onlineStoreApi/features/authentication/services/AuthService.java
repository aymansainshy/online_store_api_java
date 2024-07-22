package com.example.onlineStoreApi.features.authentication.services;

import com.example.onlineStoreApi.features.authentication.utils.AuthResponse;
import com.example.onlineStoreApi.features.authentication.utils.LoginDto;
import com.example.onlineStoreApi.features.authentication.utils.RegisterDto;


public interface AuthService {
    AuthResponse login(LoginDto loginDto);

    AuthResponse register(RegisterDto registerDto);
}
