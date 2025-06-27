package com.projectsky.loyaltysystem.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ClientDto(
        String username,
        String email,
        BigDecimal balance,
        Integer points,
        String category
) {
}
