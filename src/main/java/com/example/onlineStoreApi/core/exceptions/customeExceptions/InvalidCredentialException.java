package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialException extends CustomException {
    public InvalidCredentialException(String message) {
        super(
                message,
                HttpStatus.NOT_ACCEPTABLE,
                HttpStatus.NOT_ACCEPTABLE.value()
        );
    }

    public InvalidCredentialException() {
        super(
                HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),
                HttpStatus.NOT_ACCEPTABLE,
                HttpStatus.NOT_ACCEPTABLE.value()
        );
    }
}
