package com.example.main.unit.handler.auth.signup;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.common.application.dto.external.auth.request.SignUpRequest;
import com.example.common.application.handler.auth.signup.SignUpHandlerImpl;
import com.example.common.application.handler.role.RoleHandler;
import com.example.main.unit.AbstractUnitTest;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.detail.EmailExistsException;
import com.example.security.exception.detail.LoginExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignUpHandlerTest extends AbstractUnitTest {

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleHandler mockRoleHandler;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @InjectMocks
    private SignUpHandlerImpl signUpHandler;

    @Test
    public void test_emailExists() {
        String email = getTestUtil().TEST_USER_EMAIL;

        when(mockUserRepository.existsByEmail(email)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            signUpHandler.handle(new SignUpRequest(
                    getTestUtil().TEST_USER_LOGIN,
                    email,
                    getTestUtil().TEST_USER_PASSWORD,
                    getTestUtil().TEST_USER_FULL_NAME
            ));
        });

        String expectedMessage = EmailExistsException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), EmailExistsException.class);
        assertEquals(actualMessage, expectedMessage);

        verify(mockUserRepository, times(1))
                .existsByEmail(email);
    }

    @Test
    public void test_loginExists() {
        String email = getTestUtil().TEST_USER_EMAIL;
        String login = getTestUtil().TEST_USER_LOGIN;

        when(mockUserRepository.existsByEmail(email)).thenReturn(false);
        when(mockUserRepository.existsByLogin(login)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            signUpHandler.handle(new SignUpRequest(
                    login,
                    email,
                    getTestUtil().TEST_USER_PASSWORD,
                    getTestUtil().TEST_USER_FULL_NAME
            ));
        });

        String expectedMessage = LoginExistsException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), LoginExistsException.class);
        assertEquals(actualMessage, expectedMessage);

        verify(mockUserRepository, times(1))
                .existsByEmail(email);

        verify(mockUserRepository, times(1))
                .existsByLogin(login);
    }

    @Test
    public void test_success() {
        String email = getTestUtil().TEST_USER_EMAIL;
        String login = getTestUtil().TEST_USER_LOGIN;
        String password = getTestUtil().TEST_USER_PASSWORD;
        UserModel userModel = getTestUtil().getTestUser(RoleEnum.USER);

        when(mockUserRepository.existsByEmail(email)).thenReturn(false);
        when(mockUserRepository.existsByLogin(login)).thenReturn(false);
        when(mockRoleHandler.getRoleByNameOrCreate(RoleEnum.USER))
                .thenReturn(userModel.getRole());

        signUpHandler.handle(new SignUpRequest(
                login,
                email,
                password,
                getTestUtil().TEST_USER_FULL_NAME
        ));

        verify(mockUserRepository, times(1))
                .existsByEmail(email);

        verify(mockUserRepository, times(1))
                .existsByLogin(login);

        verify(mockPasswordEncoder, times(1))
                .encode(password);

        verify(mockRoleHandler, times(1))
                .getRoleByNameOrCreate(RoleEnum.USER);
    }
}
