package com.example.onlineStoreApi.features.authentication.services;


import com.example.onlineStoreApi.core.exceptions.customeExceptions.AuthorizationException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ConflictException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.InvalidCredentialException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.core.security.userDetailsServices.AppUserDetails;
import com.example.onlineStoreApi.features.authentication.utils.*;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.services.JwtService.JwtService;
import com.example.onlineStoreApi.services.JwtService.TokenType;
import com.example.onlineStoreApi.services.cache.CacheService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;


    public AuthResponse login(LoginDto loginDto) {
        Authentication authenticationResult = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.email(),
                        loginDto.password()
                )
        );

        if (authenticationResult.isAuthenticated()) {
            Optional<User> existingUser = userRepository.findByEmail(loginDto.email());

            if (existingUser.isEmpty()) {
                throw new ResourceNotFoundException("User not found");
            }

            String accessToken = jwtService.generateAccessToken(existingUser.get());
            String refreshToken = jwtService.generateRefreshToken(existingUser.get());

            return AuthResponse
                    .builder()
                    .user(existingUser.get())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new InvalidCredentialException();
        }

    }


    public AuthResponse register(RegisterDto registerDto) {
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());

        if (existingUser.isPresent()) {
            throw new ConflictException("User already exists");
        }

        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse
                .builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public RefreshTokenResponse refreshToken(RefreshDto refreshDto) {

        String username = jwtService.extractUsername(refreshDto.getRefresh());

        String tokenType = jwtService.extractTokenType(refreshDto.getRefresh());

        if (!Objects.equals(tokenType, TokenType.REFRESH)) {
            throw new AuthorizationException(String.format("%s token forbidden !", tokenType));
        }

        Optional<User> existingUser = userRepository.findByEmail(username);

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        String accessToken = jwtService.generateAccessToken(existingUser.get());
        var refreshToken = jwtService.generateRefreshToken(existingUser.get());

        return RefreshTokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public void logout(String token, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, authentication);
        jwtService.blacklistToken(token);
    }

}
