package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import com.example.onlineStoreApi.core.exceptions.responses.GlobalExceptionResponse;
import com.example.onlineStoreApi.core.utils.ApiResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;


@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CustomException extends RuntimeException {
    private final HttpStatus errorCode;
    private final Integer status;

    public CustomException(String message, HttpStatus errorCode, Integer status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ApiResponse<GlobalExceptionResponse> errorResponse() {
        return new ApiResponse<>(
                0,
                new GlobalExceptionResponse(this.getMessage(), this.getErrorCode(), this.getStatus()),
                this.getMessage()
        );
    }
}
