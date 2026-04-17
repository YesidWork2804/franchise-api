package com.example.franchiseapi.dto;

public record BranchResponse(
        Long id,
        String nombre,
        Long franchiseId
) {
}
