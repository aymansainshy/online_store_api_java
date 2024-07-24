package com.example.onlineStoreApi.core.exceptions.customeExceptions;

import com.example.onlineStoreApi.core.exceptions.responses.ValidationExceptionResponse;
import com.example.onlineStoreApi.core.utils.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Map;


@Getter
public class ValidationException extends CustomException {
    private final ArrayList<Map<String, String>> errors;

    public ValidationException(ArrayList<Map<String, String>> errors) {
        super(
                "Validation Exception",
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value()
        );
        this.errors = errors;
    }


    public ApiResponse<ValidationExceptionResponse> validationErrorResponse() {
        return new ApiResponse<>(
                0,
                new ValidationExceptionResponse(this.getMessage(), this.getErrorCode(), this.errors, this.getStatus()),
                this.getMessage()
        );
    }
}
