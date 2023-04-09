package com.example.admin.handler.user;

import com.example.admin.dto.external.user.response.UserResponse;
import com.example.admin.handler.AdminHandler;

import java.util.List;

public interface GetAllUsersHandler extends AdminHandler {
    /**
     * Get list with users (ordered by id DESC)
     */
    List<UserResponse> handle();
}
