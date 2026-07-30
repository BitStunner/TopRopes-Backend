package com.topropes.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Size(min = 3, max = 24)
        @Pattern(regexp = "^[a-zA-Z0-9_.-]{3,24}$")
        String username,
        @NotBlank
        @Size(min = 8, max = 100)
        String password
) {
}
