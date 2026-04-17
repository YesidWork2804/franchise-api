package com.example.franchiseapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStockRequest(
        @NotNull(message = "stock is required")
        @Min(value = 0, message = "stock must be greater than or equal to 0")
        Integer stock
) {
}
