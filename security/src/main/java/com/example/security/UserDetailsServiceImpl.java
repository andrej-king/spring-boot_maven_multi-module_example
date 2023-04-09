package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Load user by username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository
                .findByLogin(StringUtils.trimAllWhitespace(username).toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRole().getName().name())
                .disabled(!user.getEnabled())
                .accountExpired(false)
                .accountLocked(false)
                .build();
    }
}
