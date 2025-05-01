package com.uchk.university.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when there is an issue with document storage operations.
 * This will result in a 500 INTERNAL_SERVER_ERROR HTTP response.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DocumentStorageException extends RuntimeException {

    public DocumentStorageException(String message) {
        super(message);
    }

    public DocumentStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}