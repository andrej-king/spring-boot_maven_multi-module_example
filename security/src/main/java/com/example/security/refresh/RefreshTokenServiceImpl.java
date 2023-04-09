package com.example.security.refresh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.detail.RefreshTokenException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    public UserModel updateUserRefreshToken(UserModel user) {
        UUID token = UUID.randomUUID();
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

        return updateUserRefreshToken(user, token, expiresAt);
    }

    /**
     * {@inheritDoc}
     */
    public UserModel updateUserRefreshToken(UserModel user, UUID token, Instant expiresAt) {
        userRepository.saveAndFlush(user
                .setRefreshToken(token)
                .setRefreshTokenExpire(expiresAt)
        );

        return user;
    }

    /**
     * {@inheritDoc}
     */
    public UserModel verifyRefreshToken(UserModel user) throws AppException {
        /* Check token expired */
        if (Instant.now().isAfter(user.getRefreshTokenExpire())) {
            updateUserRefreshToken(user, null, null);

            throw new RefreshTokenException();
        }

        return user;
    }

}
