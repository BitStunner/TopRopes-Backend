package com.topropes.backend.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

public record MatchUpsertRequest(
        @NotBlank String name,
        @NotBlank String eventSlug,
        String feudSlug,
        @NotBlank String date,
        @NotBlank String promotion,
        @NotBlank String type,
        String stipulation,
        String duration,
        String winner,
        String card,
        @NotEmpty List<String> participants,
        List<Map<String, Object>> sides,
        List<String> entrants
) {
}
