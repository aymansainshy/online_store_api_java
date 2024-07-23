package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import org.springframework.http.HttpStatus;


public class ConflictException extends CustomException {
    public ConflictException(String message) {
        super(
                message,
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value()
        );
    }

    public ConflictException() {
        super(
                HttpStatus.CONFLICT.getReasonPhrase(),
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value()
        );
    }
}
