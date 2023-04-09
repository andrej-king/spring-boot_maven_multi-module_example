package com.example.common.application.handler.auth.signin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.common.application.dto.external.auth.request.SignInRequest;
import com.example.common.application.dto.external.auth.response.SignInResponse;
import com.example.common.domain.util.StringUtil;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.detail.LoginInfoException;
import com.example.security.exception.detail.SomethingWentWrongException;
import com.example.security.jwt.JwtTokenService;
import com.example.security.refresh.RefreshTokenService;

/**
 * Methods for sign in
 */
@Service
@RequiredArgsConstructor
public class SignInHandlerImpl implements SignInHandler {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SignInResponse handle(
            final SignInRequest signInRequest,
            final RoleEnum role
    ) throws AppException {
        Authentication authentication;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

        String login = StringUtil.cleanString(signInRequest.login(), true);
        String password = StringUtil.cleanString(signInRequest.password());

        try {
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(login, password);
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException exception) {
            throw new LoginInfoException();
        }

        /* Validate role and update refresh token */
        UserModel userModel = userRepository
                .findByLogin(login)
                .map(user -> validateUserRole(user, role))
                .map(refreshTokenService::updateUserRefreshToken)
                .orElseThrow(SomethingWentWrongException::new);

        String refreshToken = userModel.getRefreshToken().toString();
        String jwtToken = jwtTokenService.generateToken(authentication);

        return new SignInResponse(refreshToken, jwtToken);
    }

    private UserModel validateUserRole(UserModel userModel, RoleEnum role) {
        if (!userModel.getRole().getName().name().equals(role.name())) {
            throw new LoginInfoException();
        }

        return userModel;
    }
}
