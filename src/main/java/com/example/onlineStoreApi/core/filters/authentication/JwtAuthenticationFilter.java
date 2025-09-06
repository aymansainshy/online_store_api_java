package com.example.onlineStoreApi.core.filters.authentication;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.AuthorizationException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.CustomException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.InternalServerException;
import com.example.onlineStoreApi.core.security.userDetailsServices.AppUserDetails;
import com.example.onlineStoreApi.core.security.userDetailsServices.CustomUserDetailsService;
import com.example.onlineStoreApi.core.utils.StructuredLogger;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
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
//            String requestURI = request.getRequestURI();
//            boolean isGoingToPublicRoutes = requestURI.endsWith("/auth/login") || requestURI.endsWith("/auth/register") || requestURI.endsWith("/auth/refresh");
//
//            // Allow all requests to public routes, regardless of token presence
//            if (isGoingToPublicRoutes) {
//                filterChain.doFilter(request, response); // next()
//                return;
//            }


            // Require Bearer token for non-public routes
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response); // next()
                return;
                // Actually we can throw exception also, however it's better to just pass the request to next filterChain
                // and let the SecurityChainFilter handle the unauthorized access.
//                throw new AuthorizationException("Please login to get access!");
            }


            authToken = authHeader.substring(7);


            // ** IS TOKEN NOT BLACKLISTED **
            // Check if the token is not blacklisted ....
            if (jwtService.isTokenBlacklisted(authToken)) {
                filterChain.doFilter(request, response); // next()
                return;
//                throw new AuthorizationException("Token blacklisted!");
            }


            // ** IS TOKEN IS ACCESS TOKEN TYPE **
            // Extract Token type from JwtPayload aka Claim is Not RefreshToken ....
            String tokenType = jwtService.extractTokenType(authToken);
            System.out.println("___-_--________-___ ______-__-_-_-_-_-________-_---_- " + tokenType);

            if (!Objects.equals(tokenType, TokenType.ACCESS)) {
                filterChain.doFilter(request, response); // next()
                return;
//                throw new AuthorizationException(String.format("%s token forbidden !", tokenType));
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
//                                        userDetails.getPassword();
                                    userDetails.getAuthorities()
                            );

                    // This for track Who is logged into system setDetails(....)
                    usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
                    filterChain.doFilter(request, response);
                }
            }

        } catch (Exception ex) {
            /*
             * @Exceptions thrown in filters are not automatically handled by
             * Spring's @ControllerAdvice and @ExceptionHandler mechanisms.
             * Instead, you need to handle these exceptions within the filter
             * itself or use a custom Filter for centralized exception handling.
             */

            StructuredLogger.error("JWT_AUTHENTICATION_FILTER_ERROR", Map.of("error", ex.getMessage(), "stack", ex.getStackTrace()));

            if (ex instanceof CustomException) {
                response.setStatus(((CustomException) ex).getStatus());
                response.setContentType("application/json");
                response.getWriter().write(convertObjectToJson(((CustomException) ex).errorResponse()));

            } else if (ex instanceof JwtException) {
                AuthorizationException authorizationException = new AuthorizationException(ex.getMessage());
                System.out.println("r____-___ ______-__-__- ==>> " + (ex));
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
