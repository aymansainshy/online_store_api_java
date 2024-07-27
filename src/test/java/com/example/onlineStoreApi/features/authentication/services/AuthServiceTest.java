package com.example.onlineStoreApi.features.authentication.services;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.AuthorizationException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ConflictException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.InvalidCredentialException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ResourceNotFoundException;
import com.example.onlineStoreApi.core.security.userDetailsServices.AppUserDetails;
import com.example.onlineStoreApi.core.security.userDetailsServices.CustomUserDetailsService;
import com.example.onlineStoreApi.features.authentication.utils.*;
import com.example.onlineStoreApi.features.users.models.User;
import com.example.onlineStoreApi.features.users.repositories.UserRepository;
import com.example.onlineStoreApi.features.users.utils.UserRoles;
import com.example.onlineStoreApi.services.JwtService.JwtService;
import com.example.onlineStoreApi.services.JwtService.TokenType;
import com.jayway.jsonpath.InvalidCriteriaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletResponse response;
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private HttpServletRequest request;
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
                    .password("password")
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
        void testLogin_Sucsessful() {
            /* ARRANGE */
            when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
            when(passwordEncoder.encode(loginUser.getPassword())).thenReturn("password");

            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(loginUser));

            when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
            when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");


            /* ACT */
            AuthResponse authResponse = authService.login(loginDto);

            /* ASSERT */
            assertNotNull(authResponse);
            assertEquals("accessToken", authResponse.getAccessToken());
            assertEquals("refreshToken", authResponse.getRefreshToken());
            assertEquals(loginUser, authResponse.getUser());
        }

        @Test
        void testLogin_When_FindByEmail_Is_Empty_Then_Should_Throw_UserNotFoundException() {
            /* ARRANGE */
            when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
            when(userRepository.findByEmail(loginDto.email())).thenReturn(Optional.empty());

            /* ACT & ASSERT */
            assertThrows(ResourceNotFoundException.class, () -> authService.login(loginDto));
        }

        @Test
        void testLogin_When_Enter_Invalid_Credential_Then_Should_Throw_AuthorizationException() {
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
        void testRegister_Sucsessful() {
            /* ARRANGE */
            Mockito.when(userRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.empty());
            Mockito.when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
            Mockito.when(userRepository.save(any(User.class))).thenReturn(registeredUser);
            Mockito.when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
            Mockito.when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

            /* ACT */
            AuthResponse response = authService.register(registerDto);

            /* ASSERT */
            assertNotNull(response);
            assertEquals(registeredUser, response.getUser());
            assertEquals("accessToken", response.getAccessToken());
            assertEquals("refreshToken", response.getRefreshToken());
        }

        @Test
        void testRegister_When_User_Found_With_Same_Email_Then_Should_Throw_ConflictExceptions() {
            /* ARRANGE */
            when(userRepository.findByEmail(registerDto.getEmail())).thenReturn(Optional.of(registeredUser));

            /* ACT & ASSERT */
            assertThrows(ConflictException.class, () -> authService.register(registerDto));
        }
    }


    @Nested
    class RefreshTokenTests {
        private User user;
        private RefreshDto refreshDto;

        @BeforeEach
        void setUp() {
            refreshDto = new RefreshDto("refreshToken");

            user = User.builder()
                    .id(1L)
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("email@gmail.com")
                    .password("encodedPassword")
                    .build();
        }

        @Test
        void testRefreshToken_Successful() {
            /* ARRANGE */
            when(jwtService.extractTokenType(refreshDto.getRefresh())).thenReturn(TokenType.REFRESH);
            when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
            when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
            when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

            /* ACT */
            RefreshTokenResponse refreshTokenResponse = authService.refreshToken(refreshDto);

            /* ASSERT */
            assertNotNull(refreshTokenResponse);
            assertEquals("accessToken", refreshTokenResponse.getAccessToken());
            assertEquals("refreshToken", refreshTokenResponse.getRefreshToken());
        }


        @Test
        void testRefreshToken_When_The_Send_Token_Type_Is_Access_Then_Should_Throw_AuthorizationException() {
            /* ARRANGE */
            when(jwtService.extractTokenType(refreshDto.getRefresh())).thenReturn(TokenType.ACCESS);

            /* ACT & ASSERT */
            assertThrows(AuthorizationException.class, () -> authService.refreshToken(refreshDto));
        }

        @Test
        void testRefreshToken_When_The_FindByEmail_Is_Empty_Then_Should_Throw_ResourceNotFoundException() {
            /* ARRANGE */
            when(jwtService.extractTokenType(refreshDto.getRefresh())).thenReturn(TokenType.REFRESH);
            when(jwtService.extractUsername(refreshDto.getRefresh())).thenReturn(String.valueOf(Optional.empty()));
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

            /* ACT & ASSERT */
            assertThrows(ResourceNotFoundException.class, () -> authService.refreshToken(refreshDto));
        }
    }


    @Nested
    class LogoutTests {
        private String token;

        @BeforeEach
        void setUp() {
            token = "AccessToken";
            Authentication authentication = mock(Authentication.class);
            SecurityContextHolder.setContext(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
        }

        @Test
        void testLogout_Successful() {
            /* ACT */
            authService.logout(token, request, response);

            /* ASSERT */
            verify(securityContext).getAuthentication();
            verify(jwtService, times(1)).blacklistToken(token);
        }
    }
}