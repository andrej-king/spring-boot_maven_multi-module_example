package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class UUIDFormatException extends AppException {
    public static final String MSG = "Invalid token format";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public UUIDFormatException() {
        super(MSG);
    }
}
