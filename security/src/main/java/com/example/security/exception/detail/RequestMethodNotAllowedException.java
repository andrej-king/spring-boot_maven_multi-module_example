package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class RequestMethodNotAllowedException extends AppException {
    public static final String MSG = "Method not allowed";
    public static final HttpStatus STATUS = HttpStatus.METHOD_NOT_ALLOWED;

    public RequestMethodNotAllowedException() {
        super(MSG, STATUS);
    }
}
