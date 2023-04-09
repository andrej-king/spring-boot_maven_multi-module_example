package com.example.main.handler.user;

import com.example.main.dto.external.user.request.UpdatePasswordRequest;
import com.example.main.handler.MainHandler;
import com.example.security.exception.AppException;

public interface UpdatePasswordHandler extends MainHandler {
    /**
     * Update password own password (by jwt token)
     *
     * @param updatePassword contain old and new passwords
     * @param username
     * @throws AppException if invalid userId format,
     *                      user not found,
     *                      or invalid old password
     */
    void handle(final UpdatePasswordRequest updatePassword, String username) throws AppException;
}
