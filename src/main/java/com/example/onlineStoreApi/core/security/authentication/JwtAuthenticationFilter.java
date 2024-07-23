package com.example.onlineStoreApi.core.security.authentication;

import com.example.onlineStoreApi.core.security.userDetailsServices.AppUserDetails;
import com.example.onlineStoreApi.core.security.userDetailsServices.CustomUserDetailsService;
import com.example.onlineStoreApi.services.JwtService.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String authToken;
        final String userEmail;

        // ** IS TOKEN NOT PROVIDED **
        // Check if the token is null or the token Sent is not start with Bearer ....
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        authToken = authHeader.substring(7);


        // ** IS TOKEN NOT BLACKLISTED **
        // Check if the token is not blacklisted ....
        if (jwtService.isTokenBlackListed(authToken)) {
            filterChain.doFilter(request, response);
            return;
        }


        // ** IS TOKEN IS ACCESS TOKEN TYPE **
        // Extract Token type from JwtPayload aka Claim is Not RefreshToken ....
        String tokenType = jwtService.extractTokenType(authToken);
        Date expiration = jwtService.extractExpiration(authToken);
        boolean isExpired = jwtService.isTokenExpired(authToken);
        System.out.println("___-_--________-___ ______-__-_-_-_-_-________-_---_- " + tokenType);
        System.out.println("___-_--________-___ ______-__-_-_-_-_-________-_---_- " + expiration);
        System.out.println("___-_--________-___ ______-__-_-_-_-_-________-_---_- " + isExpired);

        if (Objects.equals(tokenType, "refresh")) {
            System.out.println("<<<<<<<<< ! REFRESH TOKEN FORBIDDEN ! >>>>>>>>>");
            filterChain.doFilter(request, response);
//            throw new IllegalStateException("REFRESH TOKEN FORBIDDEN");
            return;
        }


        // Extract username from JwtPayload aka Claim
        userEmail = jwtService.extractUsername(authToken);


        // if the token is valid and authenticated we need to fetch the user form DB.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            AppUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // Check if the given token and UserDetails are Valid.
            if (jwtService.isTokenValidate(authToken, userDetails)) {

                System.out.println(userDetails.getId());
                System.out.println(userDetails.getUsername());
                System.out.println(userDetails.getPassword());
                System.out.println(userDetails.getAuthorities());
                System.out.println(Arrays.toString(userDetails.getRoles()));

                UsernamePasswordAuthenticationToken usernamePasswordAuthToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
