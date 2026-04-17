package com.example.franchiseapi.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameRequest(
        @NotBlank(message = "nombre is required")
        String nombre
) {
}
