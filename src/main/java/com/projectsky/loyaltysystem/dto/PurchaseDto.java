package com.projectsky.loyaltysystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PurchaseDto(
        Long id,
        Long clientId,
        String name,
        BigDecimal price,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime purchaseDate
) {
}
