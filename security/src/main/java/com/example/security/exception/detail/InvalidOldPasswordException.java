package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class InvalidOldPasswordException extends AppException {
    public static final String MSG = "Invalid old password";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public InvalidOldPasswordException() {
        super(MSG);
    }
}
