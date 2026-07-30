package com.topropes.backend.common.api;

public record PageMeta(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
