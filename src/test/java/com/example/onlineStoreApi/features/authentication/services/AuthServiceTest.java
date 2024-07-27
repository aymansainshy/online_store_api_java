package com.example.onlineStoreApi.features.authentication.services;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.ConflictException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.InvalidCredentialException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.core.security.userDetailsServices.AppUserDetails;
import com.example.onlineStoreApi.core.security.userDetailsServices.CustomUserDetailsService;
import com.example.onlineStoreApi.features.authentication.utils.AuthResponse;
import com.example.onlineStoreApi.features.authentication.utils.LoginDto;
import com.example.onlineStoreApi.features.authentication.utils.RegisterDto;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.features.users.utils.UserRoles;
import com.example.onlineStoreApi.services.JwtService.JwtService;
import com.jayway.jsonpath.InvalidCriteriaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private JwtService jwtService;


    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
    }

    @Nested
    class LoginTests {
        private Authentication authentication;
        private LoginDto loginDto;
        private User loginUser;

        @BeforeEach
        void setUp() {
            loginDto = new LoginDto("email@gmail.com", "password");

            loginUser = User.builder()
                    .id(1L)
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("email@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .build();

            AppUserDetails userDetails = AppUserDetails
                    .builder()
                    .id(loginUser.getId())
                    .email(loginUser.getEmail())
                    .password(loginUser.getPassword())
                    .roles(loginUser.getRoles())
                    .isActive(loginUser.getIsActive())
                    .build();

            authentication = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
        }

        @Test
        void testLoginSuccess() {
            /* ARRANGE */
            when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
            when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
            when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(loginUser));

            /* ACT */
            AuthResponse authResponse = authService.login(loginDto);

            /* ASSERT */
            assertNotNull(authResponse);
            assertEquals("accessToken", authResponse.getAccessToken());
            assertEquals("refreshToken", authResponse.getRefreshToken());
            assertEquals(loginUser, authResponse.getUser());
        }

        @Test
        void testLoginUserNotFound() {
            /* ARRANGE */
            when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
            when(userRepository.findByEmail(loginDto.email())).thenReturn(Optional.empty());

            /* ACT & ASSERT */
            assertThrows(ResourceNotFoundException.class, () -> authService.login(loginDto));
        }

        @Test
        void testLoginInvalidCredentials() {
            /* ARRANGE */
            when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(
                    // This will be thrown form authentication manager if wrong credential passes
                    new InternalAuthenticationServiceException("invalid credential"));
            /* ACT & ASSERT */
            assertThrows(InternalAuthenticationServiceException.class, () -> authService.login(loginDto));
        }

    }


    @Nested
    class RegisterTests {
        private RegisterDto registerDto;
        private User registeredUser;

        @BeforeEach
        void setUp() {
            registerDto = new RegisterDto(
                    "firstName",
                    "lastName",
                    "email@gmail.com",
                    "password"
            );

            registeredUser = User.builder()
                    .id(1L)
                    .firstName(registerDto.getFirstName())
                    .lastName(registerDto.getLastName())
                    .email(registerDto.getEmail())
                    .password("encodedPassword")
                    .build();
        }

        @Test
        void testRegisterSucsessful() {
            /* ARRANGE */
            Mockito.when(userRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
            Mockito.when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
            Mockito.when(userRepository.save(any(User.class))).thenReturn(registeredUser);
            Mockito.when(jwtService.generateAccessToken(registeredUser)).thenReturn("accessToken");
            Mockito.when(jwtService.generateRefreshToken(registeredUser)).thenReturn("refreshToken");

            /* ACT */
            AuthResponse response = authService.register(registerDto);

            /* ASSERT */
            assertNotNull(response);
            assertEquals(registeredUser, response.getUser());
            assertEquals("accessToken", response.getAccessToken());
            assertEquals("refreshToken", response.getRefreshToken());
        }

        @Test
        void testRegisterUserAlreadyExists() {
            /* ARRANGE */
            when(userRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(registeredUser));

            /* ACT & ASSERT */
            assertThrows(ConflictException.class, () -> authService.register(registerDto));
        }
    }


    @Nested
    class RefreshTokenTests {
        @Test
        void refreshToken() {
        }
    }


    @Nested
    class LogoutTests {
        @Test
        void logout() {
        }
    }
}