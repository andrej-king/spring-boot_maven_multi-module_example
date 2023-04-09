package com.example.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtException;
import com.example.persistence.model.user.UserModel;

import java.util.Collection;

public interface JwtTokenService {
    /**
     * Generate JWT token from Authentication
     */
    String generateToken(Authentication authentication);

    /**
     * Generate JWT token from UserModel
     */
    String generateToken(UserModel userModel);

    /**
     * Get name from JWT token
     * <p>
     * Should be use after <b>validateToken</b> method
     * </p>
     */
    String extractUsername(String token) throws JwtException, IllegalArgumentException;

    /**
     * Get authorities from JWT token
     */
    Collection<GrantedAuthority> extractAuthorities(String token);

    /**
     * Check if token valid
     */
    boolean validateToken(String token);
}
