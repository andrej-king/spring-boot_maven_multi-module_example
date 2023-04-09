package com.example.security.refresh;

import com.example.persistence.model.user.UserModel;
import com.example.security.exception.AppException;

import java.time.Instant;
import java.util.UUID;

public interface RefreshTokenService {
    /**
     * Update Refresh token for user
     */
    UserModel updateUserRefreshToken(UserModel user);

    /**
     * Update Refresh token for user by values
     */
    UserModel updateUserRefreshToken(UserModel user, UUID token, Instant expiresAt);

    /**
     * Validate refresh token expires
     */
    UserModel verifyRefreshToken(UserModel user) throws AppException;
}
