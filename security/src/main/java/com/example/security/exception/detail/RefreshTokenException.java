package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class RefreshTokenException extends AppException {
    public static final String MSG = "Token is invalid or expired";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public RefreshTokenException() {
        super(MSG);
    }
}
