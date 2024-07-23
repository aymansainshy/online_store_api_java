package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String message) {
        super(
                message,
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value()
        );
    }

    public ResourceNotFoundException() {
        super(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value()
        );
    }
}
