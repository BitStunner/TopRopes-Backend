package com.topropes.backend.auth.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserProfileDto(
        UUID id,
        String username,
        String role,
        OffsetDateTime createdAt
) {
}
