package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomException {
    public InternalServerException() {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
    }
}
