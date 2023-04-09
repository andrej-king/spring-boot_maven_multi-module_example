package com.example.admin.unit.handler.auth.refresh;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.admin.unit.AbstractUnitTest;
import com.example.common.application.dto.external.auth.request.RefreshTokenRequest;
import com.example.common.application.handler.auth.refresh.RefreshTokenHandlerImpl;
import com.example.common.application.handler.role.RoleHandler;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.detail.RefreshTokenException;
import com.example.security.exception.detail.UUIDFormatException;
import com.example.security.jwt.JwtTokenService;
import com.example.security.refresh.RefreshTokenService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RefreshTokenHandlerTest extends AbstractUnitTest {

    @Mock
    private RefreshTokenService mockRefreshTokenService;

    @Mock
    private JwtTokenService mockJwtTokenService;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleHandler mockRoleHandler;

    @InjectMocks
    private RefreshTokenHandlerImpl refreshTokenHandler;

    @Test
    public void test_invalidFormat() {
        Exception exception = assertThrows(Exception.class, () -> {
            refreshTokenHandler.handle(new RefreshTokenRequest("abc"), RoleEnum.ADMIN);
        });

        String expectedMessage = UUIDFormatException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), UUIDFormatException.class);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void test_notFound() {
        Exception exception = assertThrows(Exception.class, () -> {
            refreshTokenHandler.handle(new RefreshTokenRequest(UUID.randomUUID().toString()), RoleEnum.ADMIN);
        });

        String expectedMessage = RefreshTokenException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), RefreshTokenException.class);
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void test_success() {
        UUID uuid = UUID.randomUUID();
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

        UserModel userModel = getTestUtil().getTestUser(RoleEnum.ADMIN);
        userModel.setRefreshToken(uuid);
        userModel.setRefreshTokenExpire(expiresAt);

        when(mockRoleHandler.getRoleByNameOrCreate(RoleEnum.ADMIN))
                .thenReturn(userModel.getRole());

        when(mockUserRepository
                .findByRefreshTokenAndRole(UUID.fromString(uuid.toString()), userModel.getRole()))
                .thenReturn(Optional.of(userModel));

        when(mockRefreshTokenService.verifyRefreshToken(userModel))
                .thenReturn(userModel);

        when(mockRefreshTokenService.updateUserRefreshToken(userModel))
                .thenReturn(userModel);

        refreshTokenHandler.handle(new RefreshTokenRequest(uuid.toString()), RoleEnum.ADMIN);

        assertNotNull(userModel);
        verify(mockRoleHandler, times(1))
                .getRoleByNameOrCreate(RoleEnum.ADMIN);

        verify(mockUserRepository, times(1))
                .findByRefreshTokenAndRole(UUID.fromString(uuid.toString()), userModel.getRole());

        verify(mockRefreshTokenService, times(1))
                .verifyRefreshToken(userModel);

        verify(mockRefreshTokenService, times(1))
                .updateUserRefreshToken(userModel);

        verify(mockJwtTokenService, times(1))
                .generateToken(userModel);
    }
}
