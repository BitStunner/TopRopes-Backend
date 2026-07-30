package com.topropes.backend.community.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record MatchRatingUpsertRequest(
        @Min(0) @Max(10) int crowd,
        @Min(0) @Max(10) int story,
        @Min(0) @Max(10) int difficulty,
        @Min(0) @Max(10) int technique,
        @Min(0) @Max(5) double personalStars,
        String review,
        @NotNull Map<String, String> notes
) {
}
