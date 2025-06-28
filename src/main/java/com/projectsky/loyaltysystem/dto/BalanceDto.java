package com.projectsky.loyaltysystem.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BalanceDto(
        @NotNull BigDecimal balance
) {
}
