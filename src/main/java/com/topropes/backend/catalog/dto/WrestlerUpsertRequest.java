package com.topropes.backend.catalog.dto;

import jakarta.validation.constraints.NotBlank;

public record WrestlerUpsertRequest(
        @NotBlank String name,
        @NotBlank String promotion,
        String tag,
        String initials,
        String height,
        String weight,
        String hometown,
        String finisher,
        String bio,
        String imageUrl
) {
}