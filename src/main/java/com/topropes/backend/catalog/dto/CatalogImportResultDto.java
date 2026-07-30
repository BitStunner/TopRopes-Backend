package com.topropes.backend.catalog.dto;

import java.util.UUID;

public record CatalogImportResultDto(
        UUID jobId,
        String status,
        String summary
) {
}
