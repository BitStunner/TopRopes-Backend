package com.topropes.backend.common.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ProblemResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        return toResponse(ex.getStatus(), ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ProblemResponse.FieldViolation> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ProblemResponse.FieldViolation(err.getField(), err.getDefaultMessage()))
                .toList();
        return toResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", request.getRequestURI(), errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ProblemResponse.FieldViolation> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new ProblemResponse.FieldViolation(v.getPropertyPath().toString(), v.getMessage()))
                .toList();
        return toResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", request.getRequestURI(), errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return toResponse(HttpStatus.FORBIDDEN, "Forbidden", request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return toResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request.getRequestURI(), null);
    }

    private ResponseEntity<ProblemResponse> toResponse(
            HttpStatus status,
            String detail,
            String instance,
            List<ProblemResponse.FieldViolation> errors
    ) {
        ProblemResponse body = new ProblemResponse(
                "about:blank",
                status.getReasonPhrase(),
                status.value(),
                detail,
                instance,
                errors
        );
        return ResponseEntity.status(status)
                .header("Content-Type", "application/problem+json")
                .body(body);
    }
}
