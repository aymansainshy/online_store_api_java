package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

    public BadRequestException(String message) {
        super(
                message,
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
    }

    public BadRequestException() {
        super(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
    }
}
