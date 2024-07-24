package com.example.onlineStoreApi.core.exceptions;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GlobalExceptionResponse {
    private final String message;
    private final HttpStatus errorCode;
    private final Integer status;

    public GlobalExceptionResponse(
            String message,
            HttpStatus errorCode,
            Integer status
    ) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
    }
}

