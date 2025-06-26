package com.projectsky.loyaltysystem.dto;

public record ErrorResponse(
        int status,
        String error,
        String message
) {
}
