package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class ForbiddenException extends AppException {
    public static final String MSG = HttpStatus.FORBIDDEN.getReasonPhrase();
    public static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public ForbiddenException() {
        super(MSG, STATUS);
    }
}
