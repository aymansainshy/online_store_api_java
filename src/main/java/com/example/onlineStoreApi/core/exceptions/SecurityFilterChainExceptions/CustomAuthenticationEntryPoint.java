package com.example.onlineStoreApi.core.exceptions.SecurityFilterChainExceptions;

import com.example.onlineStoreApi.core.exceptions.customeExceptions.AuthorizationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AuthorizationException authorizationException = new AuthorizationException( "Unauthorized - Authentication is required to access this resource.");
        response.setStatus(authorizationException.getStatus());
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(authorizationException.errorResponse()));
    }


    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
