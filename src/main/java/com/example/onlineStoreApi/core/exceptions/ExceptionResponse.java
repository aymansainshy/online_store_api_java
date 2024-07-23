package com.example.onlineStoreApi.core.exceptions;


import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Map;

@Data
public class ExceptionResponse {
    private final String message;
    private final HttpStatus errorCode;
    private final Integer status;
    private final ArrayList<Map<String, String>> errors;

    public ExceptionResponse(
            String message,
            HttpStatus errorCode,
            Integer status
    ) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.errors = new ArrayList<>();
    }

    public ExceptionResponse(
            String message,
            HttpStatus errorCode,
            ArrayList<Map<String, String>> errors,
            Integer status
    ) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.errors = errors;
    }
}

