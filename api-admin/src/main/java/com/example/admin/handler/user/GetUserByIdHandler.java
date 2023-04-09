package com.example.admin.handler.user;

import com.example.admin.dto.external.user.response.UserResponse;
import com.example.admin.handler.AdminHandler;
import com.example.common.domain.dto.external.common.request.IdRequest;
import com.example.security.exception.AppException;

public interface GetUserByIdHandler extends AdminHandler {
    /**
     * Search user by id
     *
     * @param idRequest searched user id
     * @return user
     * @throws AppException if invalid userId format,
     *                      or user not found
     */
    UserResponse handle(final IdRequest idRequest) throws AppException;
}
