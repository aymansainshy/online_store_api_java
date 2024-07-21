package com.example.onlineStoreApi.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private T data;
    private String message;

    public ApiResponse(T data, String message) {
        this.code = 1;
        this.data = data;
        this.message = message;
    }

}
