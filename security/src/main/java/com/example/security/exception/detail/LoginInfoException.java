package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class LoginInfoException extends AppException {
    public static final String MSG = "Login or password is invalid";
    public static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public LoginInfoException() {
        super(MSG, STATUS);
    }
}
