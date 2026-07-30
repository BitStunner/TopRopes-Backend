package com.topropes.backend.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EventUpsertRequest(
        @NotBlank String name,
        @NotBlank String promotion,
        @NotBlank String type,
        @NotNull LocalDate eventDate,
        String displayDate,
        String venue,
        String location
) {
}
