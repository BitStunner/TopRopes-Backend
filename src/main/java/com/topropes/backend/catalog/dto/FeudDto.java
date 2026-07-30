package com.topropes.backend.catalog.dto;

public record FeudDto(
        String slug,
        FeudParticipantDto a,
        FeudParticipantDto b,
        String status,
        int heat,
        String updated
) {
}
