package com.example.onlineStoreApi.core.exceptions;


import com.example.onlineStoreApi.core.exceptions.customeExceptions.CustomException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.InternalServerException;
import com.example.onlineStoreApi.core.exceptions.customeExceptions.ValidationException;
import com.example.onlineStoreApi.core.exceptions.responses.GlobalExceptionResponse;
import com.example.onlineStoreApi.core.exceptions.responses.ValidationExceptionResponse;
import com.example.onlineStoreApi.core.utils.ApiResponse;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConfigDataResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(
            ConfigDataResourceNotFoundException exception,
            WebRequest request) {
        System.out.println("ConfigDataResourceNotFoundException_____---_-_-----------______-___--__-__-_--_---" + (exception));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(0, ":::::::::::ConfigDataResourceNotFoundException:::::", HttpStatus.NOT_FOUND.toString())
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ValidationExceptionResponse>> handleValidationException(
            MethodArgumentNotValidException exception,
            WebRequest request) {

        ArrayList<Map<String, String>> errors = new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            Map<String, String> errorObject = new HashMap<>();
            errorObject.put(fieldName, errorMessage);

            errors.add(errorObject);
        }

        ValidationException validationException = new ValidationException(errors);
        return ResponseEntity.status(validationException.getStatus()).body(validationException.validationErrorResponse());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<GlobalExceptionResponse>> handleGlobalException(Exception exception, WebRequest request) {
        System.out.println("CustomException_____---_-_-----------______-___--__-__-_--_---" + (exception instanceof CustomException));

        if (exception instanceof CustomException) {
            return ResponseEntity.status(((CustomException) exception).getStatus()).body(((CustomException) exception).errorResponse());
        } else {
            InternalServerException internalServerException = new InternalServerException();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(internalServerException.errorResponse());
        }
    }
}
