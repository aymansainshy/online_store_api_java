package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends CustomException {
    public AuthorizationException(String message) {
        super(
                message,
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value()
        );
    }

    public AuthorizationException() {
        super(
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value()
        );
    }
}
