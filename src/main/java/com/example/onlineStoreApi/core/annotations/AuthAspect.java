package com.example.onlineStoreApi.core.annotations;


import com.example.onlineStoreApi.services.JwtService.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Aspect
@Component
public class AuthAspect {


    private final JwtService jwtService; // your JWT validator service

    public AuthAspect(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Before("@annotation(AuthRequired)")
    public void checkAuth() {
        // Get current request
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();



        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // Validate JWT (e.g., check signature, expiry, roles)
        if (!jwtService.isTokenValidate(token, "ayman@gmail.com")) {
            throw new RuntimeException("Invalid or expired token");
        }

        // ✅ If valid, user continues into the controller
        System.out.println("Auth successful for request: " + request.getRequestURI());
    }
}
