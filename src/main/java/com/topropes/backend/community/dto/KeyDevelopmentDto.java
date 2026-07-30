package com.topropes.backend.community.dto;

public record KeyDevelopmentDto(
        String date,
        String label,
        String detail,
        int heatDelta
) {
}
