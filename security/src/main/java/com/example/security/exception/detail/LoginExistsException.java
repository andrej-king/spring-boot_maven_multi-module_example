package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class LoginExistsException extends AppException {
    public static final String MSG = "This login is already in use";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public LoginExistsException() {
        super(MSG);
    }
}
