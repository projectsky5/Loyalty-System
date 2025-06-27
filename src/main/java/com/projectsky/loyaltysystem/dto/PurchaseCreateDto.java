package com.projectsky.loyaltysystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PurchaseCreateDto(
        @NotBlank String name,
        @NotBlank BigDecimal price
) {
}
