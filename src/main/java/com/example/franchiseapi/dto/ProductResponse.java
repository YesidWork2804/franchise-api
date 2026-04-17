package com.example.franchiseapi.dto;

public record ProductResponse(
        Long id,
        String nombre,
        Integer stock,
        Long branchId
) {
}
