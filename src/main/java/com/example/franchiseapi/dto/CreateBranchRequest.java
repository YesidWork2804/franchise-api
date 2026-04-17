package com.example.franchiseapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBranchRequest(
        @NotBlank(message = "nombre is required")
        String nombre
) {
}
