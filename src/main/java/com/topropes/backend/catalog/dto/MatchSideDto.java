package com.topropes.backend.catalog.dto;

import java.util.List;

public record MatchSideDto(
        String name,
        List<String> members
) {
}
