package com.topropes.backend.common.api;

import java.util.List;

public record PagedResponse<T>(
        List<T> items,
        PageMeta meta
) {
}
