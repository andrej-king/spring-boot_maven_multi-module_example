package com.example.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Class for specify custom app errors
 */
@Getter
public class AppException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
    public static final HttpStatus DEFAULT_STATUS = HttpStatus.UNPROCESSABLE_ENTITY;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public AppException(String message) {
        super(message);
        this.message = message;
        this.status = DEFAULT_STATUS;
    }
}
