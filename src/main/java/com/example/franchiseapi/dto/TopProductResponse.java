package com.example.franchiseapi.dto;

public record TopProductResponse(
        String nombreProducto,
        Integer stock,
        String nombreSucursal
) {
}
