package com.example.common.application.handler.auth.signin;

import com.example.common.application.dto.external.auth.request.SignInRequest;
import com.example.common.application.dto.external.auth.response.SignInResponse;
import com.example.common.application.handler.BaseHandler;
import com.example.persistence.model.role.RoleEnum;
import com.example.security.exception.AppException;

public interface SignInHandler extends BaseHandler {
    /**
     * Handle sign in
     */
    SignInResponse handle(
            final SignInRequest signInRequest,
            final RoleEnum role
    ) throws AppException;
}
