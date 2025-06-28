package com.projectsky.loyaltysystem.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ClientUpdateDto(

        @Size(min = 6, max = 18)
        @Pattern(regexp = "^[a-zA-Z0-9]{6,18}$")
        String username,

        @Email String email
        ) {
}
