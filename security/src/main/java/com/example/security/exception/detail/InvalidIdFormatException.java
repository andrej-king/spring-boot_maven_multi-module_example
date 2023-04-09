package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class InvalidIdFormatException extends AppException {
    public static final String MSG = "Invalid id format";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public InvalidIdFormatException() {
        super(MSG);
    }
}
