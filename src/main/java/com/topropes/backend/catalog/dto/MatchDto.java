package com.topropes.backend.catalog.dto;

import java.util.List;
import java.util.Map;

public record MatchDto(
        String slug,
        String name,
        String event,
        String eventSlug,
        String date,
        String promotion,
        String type,
        String stipulation,
        String duration,
        String winner,
        String card,
        List<String> participants,
        List<Map<String, Object>> sides,
        List<String> entrants,
        String feudSlug
) {
}
