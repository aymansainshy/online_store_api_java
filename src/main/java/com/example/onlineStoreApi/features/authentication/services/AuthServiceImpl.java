package com.example.onlineStoreApi.features.authentication.services;


import com.example.onlineStoreApi.core.exceptions.customeExceptions.AuthorizationException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ConflictException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.InvalidCredentialException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.core.filters.logging.LoggingFilter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
//@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    public AuthResponse login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.email(),
                        loginDto.password()
                )
        );

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

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthResponse
                .builder()
                .user(savedUser)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public RefreshTokenResponse refreshToken(RefreshDto refreshDto) {

        String tokenType = jwtService.extractTokenType(refreshDto.getRefresh());

        if (!Objects.equals(tokenType, TokenType.REFRESH)) {
            throw new AuthorizationException(String.format("%s token forbidden !", tokenType));
        }

        String username = jwtService.extractUsername(refreshDto.getRefresh());

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
        AppUserDetails currentUser =  (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Current User __--____--___---_-___---)))))" + currentUser);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Current authentication __--____--___---_-___---))))))" + authentication);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, authentication);
        jwtService.blacklistToken(token);
    }

}
