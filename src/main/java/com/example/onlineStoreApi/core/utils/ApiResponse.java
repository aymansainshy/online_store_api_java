package com.example.onlineStoreApi.core.utils;

import lombok.Data;
import org.springframework.http.HttpStatusCode;


@Data
public class ApiResponse<T> {
    private int code;
    private T data;
    private String message;

    public ApiResponse(T data, String message) {
        this.code = 1;
        this.data = data;
        this.message = message;
    }

    public ApiResponse(T data) {
        this.code = 1;
        this.data = data;
        this.message = "Successful";
    }

    public ApiResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
