package com.topropes.backend.catalog.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record FeudUpsertRequest(
        FeudParticipantDto a,
        FeudParticipantDto b,
        @NotBlank String status,
        @Min(0) @Max(100) int heat,
        String updated
) {
}
