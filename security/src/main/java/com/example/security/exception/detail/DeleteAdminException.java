package com.example.security.exception.detail;

import org.springframework.http.HttpStatus;
import com.example.security.exception.AppException;

public class DeleteAdminException extends AppException {
    public static final String MSG = "Admin cannot be deleted";
    public static final HttpStatus STATUS = DEFAULT_STATUS;

    public DeleteAdminException() {
        super(MSG);
    }
}
