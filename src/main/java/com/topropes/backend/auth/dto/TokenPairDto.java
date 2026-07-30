package com.topropes.backend.auth.dto;

public record TokenPairDto(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
}
