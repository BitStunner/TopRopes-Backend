package com.topropes.backend.community.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FeudPromoUpsertRequest(
        @NotNull String title,
        @NotNull String speaker,
        @NotNull LocalDate date,
        @NotNull String venue,
        @NotNull String quote,
        @NotNull String transcript,
        @NotNull String impact
) {
}
