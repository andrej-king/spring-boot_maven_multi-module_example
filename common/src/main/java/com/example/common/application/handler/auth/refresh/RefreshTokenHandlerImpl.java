package com.example.common.application.handler.auth.refresh;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.example.common.application.dto.external.auth.request.RefreshTokenRequest;
import com.example.common.application.dto.external.auth.response.SignInResponse;
import com.example.common.application.handler.role.RoleHandler;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.detail.RefreshTokenException;
import com.example.security.exception.detail.UUIDFormatException;
import com.example.security.jwt.JwtTokenService;
import com.example.security.refresh.RefreshTokenService;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class RefreshTokenHandlerImpl implements RefreshTokenHandler {
    private final UserRepository userRepository;
    private final RoleHandler roleHandler;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenService jwtTokenService;

    @Override
    public SignInResponse handle(
            final RefreshTokenRequest refreshTokenRequest,
            RoleEnum role
    ) throws AppException {

        /* Search user by refresh token and validate token expires */
        try {
            UserModel user = userRepository
                    .findByRefreshTokenAndRole(
                            UUID.fromString(refreshTokenRequest.refreshToken()),
                            roleHandler.getRoleByNameOrCreate(role)
                    )
                    .map(refreshTokenService::verifyRefreshToken)
                    .map(refreshTokenService::updateUserRefreshToken)
                    .orElseThrow(RefreshTokenException::new);

            String refreshToken = user.getRefreshToken().toString();
            String jwtToken = jwtTokenService.generateToken(user);

            return new SignInResponse(refreshToken, jwtToken);
        } catch (IllegalArgumentException exception) {
            throw new UUIDFormatException();
        }
    }
}
