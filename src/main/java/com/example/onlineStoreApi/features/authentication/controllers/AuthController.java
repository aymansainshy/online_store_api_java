package com.example.onlineStoreApi.features.authentication.controllers;


import com.example.onlineStoreApi.core.utils.ApiResponse;
import com.example.onlineStoreApi.features.authentication.services.AuthService;
import com.example.onlineStoreApi.features.authentication.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
//    @Cacheable(cacheNames="user", key="#email")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginDto loginDto) {
        AuthResponse authResponse = authService.login(loginDto);
        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(authResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterDto registerDto) {
        AuthResponse authResponse = authService.register(registerDto);
        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(authResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@Valid @RequestBody RefreshDto refreshDto) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(refreshDto);
        ApiResponse<RefreshTokenResponse> apiResponse = new ApiResponse<>(refreshTokenResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") @NotNull String token, HttpServletRequest request, HttpServletResponse response) {
        String extractedToken = token.substring(7);
        authService.logout(extractedToken, request, response);
        ApiResponse<String> apiResponse = new ApiResponse<>("Successful");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
    }
}



