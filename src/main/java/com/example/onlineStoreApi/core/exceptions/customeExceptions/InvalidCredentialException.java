package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialException extends CustomException {
    public InvalidCredentialException(String message) {
        super(
                message,
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value()
        );
    }

    public InvalidCredentialException() {
        super(
                "invalid credential",
                HttpStatus.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value()
        );
    }
}
