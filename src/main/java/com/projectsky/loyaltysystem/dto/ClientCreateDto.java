package com.projectsky.loyaltysystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ClientCreateDto(

        @NotBlank
        @Size(min = 6, max = 18)
        @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$")
        String username,

        @NotBlank
        @Email
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "Email must be valid and strictly formatted")
        String email
) {
}
