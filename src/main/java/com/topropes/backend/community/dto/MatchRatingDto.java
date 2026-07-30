package com.topropes.backend.community.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record MatchRatingDto(
        String matchSlug,
        int crowd,
        int story,
        int difficulty,
        int technique,
        double personalStars,
        String review,
        Map<String, String> notes,
        OffsetDateTime updatedAt
) {
}
