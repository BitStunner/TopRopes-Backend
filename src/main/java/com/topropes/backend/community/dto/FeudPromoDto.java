package com.topropes.backend.community.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record FeudPromoDto(
        String feudSlug,
        String promoSlug,
        String title,
        String speaker,
        LocalDate date,
        String venue,
        String quote,
        String transcript,
        String impact,
        OffsetDateTime updatedAt
) {
}
