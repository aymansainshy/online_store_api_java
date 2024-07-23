package com.example.onlineStoreApi.features.authentication.utils;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDto {
    @NotBlank(message = "Refresh token is mandatory")
    private String refresh;
}
