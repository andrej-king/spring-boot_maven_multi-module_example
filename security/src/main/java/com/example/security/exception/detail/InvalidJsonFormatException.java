package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class InvalidJsonFormatException extends AppException {
    public static final String MSG = "Invalid JSON format";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public InvalidJsonFormatException() {
        super(MSG);
    }
}
