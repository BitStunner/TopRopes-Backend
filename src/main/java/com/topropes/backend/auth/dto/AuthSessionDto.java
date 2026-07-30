package com.topropes.backend.auth.dto;

public record AuthSessionDto(
        UserProfileDto user,
        TokenPairDto tokens
) {
}
