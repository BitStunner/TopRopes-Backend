package com.topropes.backend.community.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record FeudDossierDto(
        String feudSlug,
        String participants,
        String cause,
        String motivationA,
        String motivationB,
        String background,
        String stakes,
        String currentStatus,
        String resolution,
        List<KeyDevelopmentDto> keyDevelopments,
        int heat,
        OffsetDateTime updatedAt
) {
}
