package com.topropes.backend.common.error;

import java.util.List;

public record ProblemResponse(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        List<FieldViolation> errors
) {
    public record FieldViolation(String field, String message) {
    }
}
