package com.example.common.application.handler.auth.signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.common.application.dto.external.auth.request.SignUpRequest;
import com.example.common.application.dto.external.auth.response.SignUpResponse;
import com.example.common.application.handler.role.RoleHandler;
import com.example.common.domain.util.StringUtil;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.detail.EmailExistsException;
import com.example.security.exception.detail.LoginExistsException;

/**
 * Methods for sign up
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class SignUpHandlerImpl implements SignUpHandler {
    private final UserRepository userRepository;
    private final RoleHandler roleHandler;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public SignUpResponse handle(final SignUpRequest signUpRequest) throws AppException {
        String email = StringUtil.cleanString(signUpRequest.email(), true);
        String login = StringUtil.cleanString(signUpRequest.login(), true);
        String fullName = StringUtil.cleanString(signUpRequest.fullName());
        String password = StringUtil.cleanString(signUpRequest.password());

        /* Check email duplicate */
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException();
        }

        /* Check login duplicate */
        if (userRepository.existsByLogin(login)) {
            throw new LoginExistsException();
        }

        UserModel user = UserModel.builder()
                .login(login)
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .role(roleHandler.getRoleByNameOrCreate(RoleEnum.USER))
                .build();

        userRepository.saveAndFlush(user);

        log.debug(String.format(
                "Created user [id = %d], [login = %s], [email = %s]",
                user.getId(),
                user.getLogin(),
                user.getEmail()
        ));

        return SignUpResponse.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .login(user.getLogin())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}
