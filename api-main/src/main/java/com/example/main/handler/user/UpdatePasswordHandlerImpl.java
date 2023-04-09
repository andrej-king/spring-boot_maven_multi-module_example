package com.example.main.handler.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.common.domain.util.StringUtil;
import com.example.main.dto.external.user.request.UpdatePasswordRequest;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.detail.InvalidOldPasswordException;
import com.example.security.exception.detail.ResourceNotFoundException;

import java.time.Instant;

@Log4j2
@Service
@RequiredArgsConstructor
public class UpdatePasswordHandlerImpl implements UpdatePasswordHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(
            final UpdatePasswordRequest updatePassword,
            final String username
    ) throws AppException {
        String cleanUsername = StringUtil.cleanString(username, true);
        String oldPassword = StringUtil.cleanString(updatePassword.oldPassword());
        String newPassword = StringUtil.cleanString(updatePassword.newPassword());

        UserModel userEntity = userRepository.findByLogin(cleanUsername)
                .orElseThrow(ResourceNotFoundException::new);

        /* Check old password */
        if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            throw new InvalidOldPasswordException();
        }

        Instant beforeChanges = userEntity.getUpdatedAt();

        /* Update password */
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userEntity.setUpdatedAt(Instant.now());
        userRepository.saveAndFlush(userEntity);

        Instant afterChanges = userEntity.getUpdatedAt();

        log.debug("Updated user password = " + cleanUsername);
        log.debug("Before changes = " + beforeChanges.toString());
        log.debug("After changes = " + afterChanges.toString());
    }
}
