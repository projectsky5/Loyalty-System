package com.projectsky.loyaltysystem.dto;

import lombok.Builder;

@Builder
public record ClientDto(
        String username,
        String email,
        Integer points,
        String category
) {
}
