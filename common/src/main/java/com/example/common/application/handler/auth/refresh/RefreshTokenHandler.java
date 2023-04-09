package com.example.common.application.handler.auth.refresh;

import com.example.common.application.dto.external.auth.request.RefreshTokenRequest;
import com.example.common.application.dto.external.auth.response.SignInResponse;
import com.example.common.application.handler.BaseHandler;
import com.example.persistence.model.role.RoleEnum;
import com.example.security.exception.AppException;

public interface RefreshTokenHandler extends BaseHandler {
    SignInResponse handle(
            final RefreshTokenRequest refreshTokenRequest,
            final RoleEnum role
    ) throws AppException;
}
