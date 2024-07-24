package com.example.onlineStoreApi.features.authentication.services;


import com.example.onlineStoreApi.core.exceptions.customeExceptions.ConflictException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.features.authentication.utils.*;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.services.JwtService.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;


    public AuthResponse login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        Optional<User> existingUser = userRepository.findByEmail(loginDto.getEmail());

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

//        boolean isMatch = passwordEncoder.matches(loginDto.getPassword(), existingUser.get().getPassword());
//        if (!isMatch) {
//            throw new IllegalStateException("Wrong Credential");
//        }

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
    public RefreshTokenResponse refreshToken(RefreshDto refreshDto)  {
        System.out.println("___-_--________-___ ____REFRESH__-__-_-_-_-_-________-_---_- " + refreshDto.getRefresh());
        var username = jwtService.extractUsername(refreshDto.getRefresh());


        String tokenType = jwtService.extractTokenType(refreshDto.getRefresh());
        Date expiration = jwtService.extractExpiration(refreshDto.getRefresh());
        boolean isExpired = jwtService.isTokenExpired(refreshDto.getRefresh());
        System.out.println("___-_--________-___ ____REFRESH__-__-_-_-_-_-________-_---_- " + tokenType);
        System.out.println("___-_--________-___ ____REFRESH-__-_-_-_-_-________-_---_- " + expiration);
        System.out.println("___-_--________-___ ____REFRESH-__-_-_-_-_-________-_---_- " + isExpired);

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

}
