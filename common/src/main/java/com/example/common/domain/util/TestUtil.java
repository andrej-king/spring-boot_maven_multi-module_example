package com.example.common.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.common.application.handler.role.RoleHandler;
import com.example.common.application.middleware.AppExceptionHandler;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.role.RoleModel;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.ErrorMessages;
import com.example.security.jwt.JwtTokenService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

/**
 * Utils for testing
 */
@Component
@RequiredArgsConstructor
public class TestUtil {
    private final AppExceptionHandler exceptionHandler;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final RoleHandler roleHandler;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationContext applicationContext;

    @Value("${test.reset.sql.identity}")
    private String resetSqlIdentity;

    public final String TEST_ADMIN_EMAIL = "test@example.com";
    public final String TEST_ADMIN_LOGIN = "john";
    public final String TEST_ADMIN_FULL_NAME = "John Doe";
    public final String TEST_ADMIN_PASSWORD = "somePassword";

    @Getter
    private Long adminId;

    @Getter
    private Long adminRoleId;

    public final String TEST_USER_EMAIL = "test2@example.com";
    public final String TEST_USER_LOGIN = "john2";
    public final String TEST_USER_FULL_NAME = "John Doe 2";
    public final String TEST_USER_PASSWORD = "somePassword2";

    @Getter
    private Long userId;

    @Getter
    private Long userRoleId;

    /**
     * Convert app exception object to the JSON string body
     */
    public final String getAppExceptionBodyAsJsonString(AppException exception) {
        ErrorMessages exceptionBody = exceptionHandler.handleAppException(exception).getBody();

        return asJsonString(exceptionBody);
    }

    /**
     * Convert object to the JSON string
     */
    public final String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Replace rote variable to the value
     * example call: replaceRouteVariable("/users/{id}", "id", "1")
     *
     * @return example: /users/{id} -> /users/1
     */
    public final String replaceRouteVariable(String route, String variableName, String value) {
        return route.replace("{" + variableName + "}", value);
    }

    /**
     * Get basic rest http headers
     */
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    /**
     * Add auth header to the exists
     */
    public HttpHeaders addAuthHeaders(HttpHeaders headers, RoleEnum role) {
        String jwtToken = jwtTokenService.generateToken(getTestUser(role));
        assertNotNull(jwtToken);

        headers.add("Authorization", "Bearer " + jwtToken);

        return headers;
    }

    /**
     * Get test user with role
     */
    public UserModel getTestUser(RoleEnum role) {
        RoleModel roleModel = RoleModel.builder()
                .name(role)
                .build();

        String login = TEST_ADMIN_LOGIN;
        String email = TEST_ADMIN_EMAIL;
        String password = TEST_ADMIN_PASSWORD;
        String fullName = TEST_ADMIN_FULL_NAME;

        if (RoleEnum.USER.name().equals(role.name())) {
            login = TEST_USER_LOGIN;
            email = TEST_USER_EMAIL;
            password = TEST_USER_PASSWORD;
            fullName = TEST_USER_FULL_NAME;
        }

        return UserModel.builder()
                .login(login)
                .email(email)
                .password(password)
                .fullName(fullName)
                .role(roleModel)
                .build();
    }

    /**
     * Reset table identity
     */
    public void resetAutoIncrementColumns(String... tableNames) {
        DataSource dataSource = applicationContext.getBean(DataSource.class);

        try (Connection dbConnection = dataSource.getConnection()) {
            // Create SQL statements that reset the auto increment columns and invoke
            // the created SQL statements.
            for (String tableName : tableNames) {
                try (Statement statement = dbConnection.createStatement()) {
                    String sql = String.format(resetSqlIdentity, tableName);
                    statement.execute(sql);
                }
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Insert 1 user with role "ADMIN"
     */
    public void insertTestAdmin() {
        RoleModel adminRole = roleHandler.getRoleByNameOrCreate(RoleEnum.ADMIN);
        adminRoleId = adminRole.getId();

        UserModel newUser = UserModel.builder()
                .login(TEST_ADMIN_LOGIN)
                .email(TEST_ADMIN_EMAIL)
                .password(passwordEncoder.encode(TEST_ADMIN_PASSWORD))
                .fullName(TEST_ADMIN_FULL_NAME)
                .role(adminRole)
                .build();

        userRepository.saveAndFlush(newUser);
        adminId = newUser.getId();
    }

    /**
     * Insert 1 user with role "USER"
     */
    public void insertTestUser() {
        RoleModel userRole = roleHandler.getRoleByNameOrCreate(RoleEnum.USER);
        userRoleId = userRole.getId();

        UserModel newUser = UserModel.builder()
                .login(TEST_USER_LOGIN)
                .email(TEST_USER_EMAIL)
                .password(passwordEncoder.encode(TEST_USER_PASSWORD))
                .fullName(TEST_USER_FULL_NAME)
                .role(userRole)
                .build();

        userRepository.saveAndFlush(newUser);
        userId = newUser.getId();
    }
}
