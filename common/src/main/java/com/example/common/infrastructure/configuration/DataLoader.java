package com.example.common.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.common.application.handler.role.RoleHandler;
import com.example.common.domain.util.AppUtil;
import com.example.common.domain.util.StringUtil;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.role.RoleModel;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;

@Log4j2
@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleHandler roleHandler;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ConfigurableEnvironment env;

    @Override
    public void run(ApplicationArguments args) {
        String activeProfile = env.getActiveProfiles()[0];

        log.debug("Called: " + DataLoader.class);
        log.debug("Active profile DataLoader: " + activeProfile);

        /* Create a new admin user if DEV env */
        String email = StringUtil.cleanString("test@example.com", true);
        if (activeProfile.equals(AppUtil.PROFILE_DEV) && !userRepository.existsByEmail(email)) {
            RoleModel adminRole = roleHandler.getRoleByNameOrCreate(RoleEnum.ADMIN);
            roleHandler.getRoleByNameOrCreate(RoleEnum.USER);

            String login = StringUtil.cleanString("John", true);
            String fullName = StringUtil.cleanString("John Doe");
            String password = StringUtil.cleanString("somePassword");

            log.debug("Create new admin after load app");

            UserModel newUser = UserModel.builder()
                    .login(login)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .fullName(fullName)
                    .role(adminRole)
                    .build();

            userRepository.saveAndFlush(newUser);
        }
    }
}
