package com.example.onlineStoreApi.features.authentication.utils;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshDto {
    @NotBlank(message = "Refresh token is mandatory")
    private String refresh;
}
