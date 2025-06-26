package com.projectsky.loyaltysystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClientFullDto(
        Long id,
        String name,
        Integer points,
        String category,
        Integer totalPurchases,
        LocalDateTime lastPurchase
) {
}
