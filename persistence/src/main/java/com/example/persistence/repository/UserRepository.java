package com.example.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.persistence.model.role.RoleModel;
import com.example.persistence.model.user.UserModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    /**
     * Find user by email
     */
    Optional<UserModel> findByEmail(String email);

    /**
     * Find user by login
     */
    Optional<UserModel> findByLogin(String login);

    /**
     * Find user by refresh token
     */
    Optional<UserModel> findByRefreshTokenAndRole(UUID token, RoleModel role);

    /**
     * Check if user exists by email
     */
    Boolean existsByEmail(String email);

    /**
     * Check if user exists by login
     */
    Boolean existsByLogin(String login);
}
