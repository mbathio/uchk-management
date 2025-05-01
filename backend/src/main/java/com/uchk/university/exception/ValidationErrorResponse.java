package com.uchk.university.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Extended error response for validation errors, including field-specific error details.
 */
@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, LocalDateTime timestamp, String message, String path, Map<String, String> errors) {
        super(status, timestamp, message, path);
        this.errors = errors;
    }
}