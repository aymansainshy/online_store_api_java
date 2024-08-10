package com.example.onlineStoreApi.core.exceptions;


import com.example.onlineStoreApi.core.exceptions.customeExceptions.*;
import com.example.onlineStoreApi.core.exceptions.responses.GlobalExceptionResponse;
import com.example.onlineStoreApi.core.exceptions.responses.ValidationExceptionResponse;
import com.example.onlineStoreApi.core.utils.ApiResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ValidationExceptionResponse>> handleValidationException(
            MethodArgumentNotValidException exception,
            WebRequest request) {

        ArrayList<Map<String, String>> errors = new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            Map<String, String> errorObject = new HashMap<>();
            errorObject.put(fieldName, errorMessage);

            errors.add(errorObject);
        }

        ValidationException validationException = new ValidationException(errors);
        return ResponseEntity.status(validationException.getStatus()).body(validationException.validationErrorResponse());
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<GlobalExceptionResponse>> handleGlobalException(Exception exception, WebRequest request) {

        switch (exception) {
            case CustomException customException -> {
                return ResponseEntity.status(customException.getStatus()).body(customException.errorResponse());
            }

            case BadCredentialsException badCredentialsException -> {
                InvalidCredentialException invalidCredentialException = new InvalidCredentialException(badCredentialsException.getMessage());
                return ResponseEntity.status(invalidCredentialException.getStatus()).body(invalidCredentialException.errorResponse());
            }

            case InternalAuthenticationServiceException internalAuthenticationServiceException -> {
                ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException(internalAuthenticationServiceException.getMessage());
                return ResponseEntity.status(resourceNotFoundException.getStatus()).body(resourceNotFoundException.errorResponse());
            }

            case JwtException jwtException -> {
                AuthorizationException authorizationException = new AuthorizationException(jwtException.getMessage());
                return ResponseEntity.status(authorizationException.getStatus()).body(authorizationException.errorResponse());
            }

            case null, default -> {
                assert exception != null;
                InternalServerException internalServerException = new InternalServerException(exception.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerException.errorResponse());
            }
        }
    }
}
