package com.topropes.backend.catalog.dto;

public record WrestlerDto(
        String slug,
        String name,
        String promotion,
        String tag,
        String initials,
        String imageUrl,
        String height,
        String weight,
        String hometown,
        String finisher,
        String bio
) {
}
