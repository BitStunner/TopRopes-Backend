package com.topropes.backend.catalog.dto;

import java.time.LocalDate;
import java.util.List;

public record EventDto(
        String slug,
        String name,
        String promotion,
        String type,
        LocalDate eventDate,
        String displayDate,
        String venue,
        String location,
        List<String> matchSlugs
) {
}
