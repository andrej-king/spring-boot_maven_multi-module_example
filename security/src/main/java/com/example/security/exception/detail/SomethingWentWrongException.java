package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class SomethingWentWrongException extends AppException {
    public static final String MSG = "Something went wrong";
    public static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public SomethingWentWrongException() {
        super(MSG, STATUS);
    }
}
