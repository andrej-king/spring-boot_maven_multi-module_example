package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class ResourceNotFoundException extends AppException {
    public static final String MSG = "Resource not found";
    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException() {
        super(MSG, STATUS);
    }
}
