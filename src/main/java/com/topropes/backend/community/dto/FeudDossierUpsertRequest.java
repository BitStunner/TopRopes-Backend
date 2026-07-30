package com.topropes.backend.community.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FeudDossierUpsertRequest(
        @NotNull String participants,
        @NotNull String cause,
        @NotNull String motivationA,
        @NotNull String motivationB,
        @NotNull String background,
        @NotNull String stakes,
        @NotNull String currentStatus,
        @NotNull String resolution,
        @NotNull List<KeyDevelopmentDto> keyDevelopments,
        @Min(0) @Max(100) int heat
) {
}
