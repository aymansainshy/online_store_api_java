package com.example.onlineStoreApi.core.filters.authentication;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.AuthorizationException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.CustomException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.InternalServerException;
import com.example.onlineStoreApi.core.security.userDetailsServices.AppUserDetails;
import com.example.onlineStoreApi.core.security.userDetailsServices.CustomUserDetailsService;
import com.example.onlineStoreApi.services.JwtService.JwtService;
import com.example.onlineStoreApi.services.JwtService.TokenType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
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
            @NonNull FilterChain filterChain // Equivalent to next()
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String authToken;
            final String userEmail;

            // ** IS TOKEN NOT PROVIDED **
            // Check if the token is null or the token send is not start with Bearer ....
            String reqPath = request.getRequestURI();
            boolean publicPath = reqPath.endsWith("/auth/login") || reqPath.endsWith("/auth/register") || reqPath.endsWith("/auth/refresh");

            // If the user going to public path but not provide token let go, else Throw exception
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                if (publicPath) {
                    filterChain.doFilter(request, response); // next()
                    return;
                } else {
                    throw new AuthorizationException("Token Not Provided");
                }
            }

            // If the user going to public path, and he provide Token in request let go
            if (publicPath) {
                filterChain.doFilter(request, response); // next()
                return;
            }


            authToken = authHeader.substring(7);


            // ** IS TOKEN NOT BLACKLISTED **
            // Check if the token is not blacklisted ....
            if (jwtService.isTokenBlacklisted(authToken)) {
                throw new AuthorizationException("Token Blacklisted!");
            }


            // ** IS TOKEN IS ACCESS TOKEN TYPE **
            // Extract Token type from JwtPayload aka Claim is Not RefreshToken ....
            String tokenType = jwtService.extractTokenType(authToken);
            Date expiration = jwtService.extractExpiration(authToken);
            System.out.println("___-_--________-___ ______-__-_-_-_-_-________-_---_- " + tokenType);
            System.out.println("___-_--________-___ ______-__-_-_-_-_-________-_---_- " + expiration);

            if (!Objects.equals(tokenType, TokenType.ACCESS)) {
                throw new AuthorizationException(String.format("%s token forbidden !", tokenType));
            }


            // Extract username from JwtPayload aka Claim
            userEmail = jwtService.extractUsername(authToken);


            // if the token is valid and authenticated we need to fetch the user form DB.
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                AppUserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // Check if the given token and UserDetails are Valid.
                if (jwtService.isTokenValidate(authToken, userDetails.getEmail())) {

                    System.out.println(userDetails.getId());
                    System.out.println(userDetails.getUsername());
                    System.out.println(userDetails.getAuthorities());

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

        } catch (Exception ex) {
            /*
             * @Exceptions thrown in filters are not automatically handled by Spring's
             * @ControllerAdvice and @ExceptionHandler mechanisms.
             * Instead, you need to handle these exceptions within the filter
             * itself or use a custom Filter for centralized exception handling.
             */
            System.out.println("___-_--____Authentication filter____-___ ______-__-__- ==>> " + (ex));

            if (ex instanceof CustomException) {
                response.setStatus(((CustomException) ex).getStatus());
                response.setContentType("application/json");
                response.getWriter().write(convertObjectToJson(((CustomException) ex).errorResponse()));

            } else if (ex instanceof JwtException) {
                AuthorizationException authorizationException = new AuthorizationException(ex.getMessage());
                response.setStatus(authorizationException.getStatus());
                response.setContentType("application/json");
                response.getWriter().write(convertObjectToJson(authorizationException.errorResponse()));

            } else {
                InternalServerException internalServerException = new InternalServerException();
                response.setStatus(internalServerException.getStatus());
                response.setContentType("application/json");
                response.getWriter().write(convertObjectToJson(internalServerException.errorResponse()));
            }

            return;
        }

        filterChain.doFilter(request, response);
    }


    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
