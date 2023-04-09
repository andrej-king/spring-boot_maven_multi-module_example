package com.example.common.application.handler.auth.signup;

import com.example.common.application.dto.external.auth.request.SignUpRequest;
import com.example.common.application.dto.external.auth.response.SignUpResponse;
import com.example.common.application.handler.BaseHandler;
import com.example.security.exception.AppException;

public interface SignUpHandler extends BaseHandler {
    /**
     * Create a new user
     */
    SignUpResponse handle(final SignUpRequest signUpRequest) throws AppException;
}
