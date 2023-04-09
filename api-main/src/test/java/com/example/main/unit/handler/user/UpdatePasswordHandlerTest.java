package com.example.main.unit.handler.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.main.dto.external.user.request.UpdatePasswordRequest;
import com.example.main.handler.user.UpdatePasswordHandlerImpl;
import com.example.main.unit.AbstractUnitTest;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.detail.InvalidOldPasswordException;
import com.example.security.exception.detail.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdatePasswordHandlerTest extends AbstractUnitTest {
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdatePasswordHandlerImpl handler;

    @Test
    public void test_updatePassword_userNotFound() {
        Exception exception = assertThrows(Exception.class, () -> {
            handler.handle(
                    new UpdatePasswordRequest("old", "new"),
                    "username"
            );
        });

        String expectedMessage = ResourceNotFoundException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), ResourceNotFoundException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_updatePassword_passwordMismatch() {
        UserModel userModel = getTestUtil().getTestUser(RoleEnum.USER);
        String oldPassword = userModel.getPassword();
        String login = "username";

        when(mockUserRepository.findByLogin(login)).thenReturn(Optional.ofNullable(userModel));
        when(passwordEncoder.matches(oldPassword, oldPassword)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            handler.handle(new UpdatePasswordRequest(oldPassword, "new"), login);
        });

        verify(mockUserRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);

        String expectedMessage = InvalidOldPasswordException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), InvalidOldPasswordException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_updatePassword_success() {
        UserModel userModel = getTestUtil().getTestUser(RoleEnum.USER);
        String oldPassword = userModel.getPassword();
        String newPassword = "new";
        String login = "username";

        when(mockUserRepository.findByLogin(login)).thenReturn(Optional.of(userModel));
        when(passwordEncoder.matches(oldPassword, oldPassword)).thenReturn(true);

        handler.handle(new UpdatePasswordRequest(userModel.getPassword(), newPassword), login);

        verify(mockUserRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(mockUserRepository, times(1)).saveAndFlush(userModel);
    }
}
