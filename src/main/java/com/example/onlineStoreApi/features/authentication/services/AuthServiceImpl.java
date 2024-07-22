package com.example.onlineStoreApi.features.authentication.services;


import com.example.onlineStoreApi.core.appUser.ApplicationUser;
import com.example.onlineStoreApi.features.authentication.utils.AuthResponse;
import com.example.onlineStoreApi.features.authentication.utils.LoginDto;
import com.example.onlineStoreApi.features.authentication.utils.RegisterDto;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.services.JwtService.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


    public AuthResponse login(LoginDto loginDto) throws IllegalStateException {
        Optional<User> existingUser = userRepository.findByEmail(loginDto.getEmail());

        if (existingUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        boolean isMatch = passwordEncoder.matches(loginDto.getPassword(), existingUser.get().getPassword());
        if (!isMatch) {
            throw new IllegalStateException("Wrong Credential");
        }

        UserDetails userDetails = ApplicationUser
                .builder()
                .email(existingUser.get().getEmail())
                .password(existingUser.get().getPassword())
                .roles(existingUser.get().getRoles())
                .isActive(existingUser.get().getIsActive())
                .build();

        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse
                .builder()
                .user(existingUser.get())
                .accessToken(jwtToken)
                .build();
    }


    public AuthResponse register(RegisterDto registerDto) throws IllegalStateException {
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());

        if (existingUser.isPresent()) {
            throw new IllegalStateException("User already exists");
        }

        User user = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        userRepository.save(user);

        UserDetails userDetails = ApplicationUser
                .builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .build();

        var jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse
                .builder()
                .user(user)
                .accessToken(jwtToken)
                .build();
    }

}
