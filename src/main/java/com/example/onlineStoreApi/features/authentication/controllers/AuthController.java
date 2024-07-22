package com.example.onlineStoreApi.features.authentication.controllers;


import com.example.onlineStoreApi.core.utils.ApiResponse;
import com.example.onlineStoreApi.features.authentication.services.AuthService;
import com.example.onlineStoreApi.features.authentication.utils.AuthResponse;
import com.example.onlineStoreApi.features.authentication.utils.LoginDto;
import com.example.onlineStoreApi.features.authentication.utils.RegisterDto;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.utils.CreateUserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Validated
            @RequestBody
            LoginDto loginDto
    ) throws IllegalStateException {
        AuthResponse authResponse = authService.login(loginDto);
        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(authResponse, "User loggedIn successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Validated
            @RequestBody
            RegisterDto registerDto
    ) throws IllegalStateException {
        AuthResponse authResponse = authService.register(registerDto);
        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(authResponse, "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
