package com.example.admin.handler.user;

import com.example.admin.handler.AdminHandler;
import com.example.common.domain.dto.external.common.request.IdRequest;
import com.example.security.exception.AppException;

public interface DeleteUserByIdHandler extends AdminHandler {
    /**
     * Delete user by id
     *
     * @param idRequest searched user id
     * @throws AppException if invalid userId format,
     *                      or user not found
     */
    void handle(final IdRequest idRequest) throws AppException;
}
