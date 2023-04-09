package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class UnauthorizedException extends AppException {
    public static final String MSG = HttpStatus.UNAUTHORIZED.getReasonPhrase();
    public static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException() {
        super(MSG, STATUS);
    }
}
