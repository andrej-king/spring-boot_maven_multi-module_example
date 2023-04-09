package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class EmailExistsException extends AppException {
    public static final String MSG = "This email is already in use";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public EmailExistsException() {
        super(MSG);
    }
}
