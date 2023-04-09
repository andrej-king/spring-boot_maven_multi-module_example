package com.example.admin.unit.handler.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.admin.handler.user.DeleteUserByIdHandlerImpl;
import com.example.admin.unit.AbstractUnitTest;
import com.example.common.domain.dto.external.common.request.IdRequest;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.detail.DeleteAdminException;
import com.example.security.exception.detail.InvalidIdFormatException;
import com.example.security.exception.detail.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteUserByIdHandlerTest extends AbstractUnitTest {
    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private DeleteUserByIdHandlerImpl deleteUserByIdHandler;

    @Test
    public void test_deleteById_invalidIdFormat() {
        Exception exception = assertThrows(Exception.class, () -> {
            deleteUserByIdHandler.handle(new IdRequest("abc"));
        });

        String expectedMessage = InvalidIdFormatException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), InvalidIdFormatException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_deleteById_notFound() {
        Exception exception = assertThrows(Exception.class, () -> {
            deleteUserByIdHandler.handle(new IdRequest("1"));
        });

        String expectedMessage = ResourceNotFoundException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), ResourceNotFoundException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_deleteById_adminException() {
        UserModel userModel = getTestUtil().getTestUser(RoleEnum.ADMIN);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.ofNullable(userModel));

        Exception exception = assertThrows(Exception.class, () -> {
            deleteUserByIdHandler.handle(new IdRequest("1"));
        });

        verify(mockUserRepository, times(1)).findById(1L);

        String expectedMessage = DeleteAdminException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), DeleteAdminException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_deleteById_success() {
        UserModel userModel = getTestUtil().getTestUser(RoleEnum.USER);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.ofNullable(userModel));

        deleteUserByIdHandler.handle(new IdRequest("1"));

        assertNotNull(userModel);
        verify(mockUserRepository, times(1)).findById(1L);
        verify(mockUserRepository, times(1)).delete(userModel);
    }
}
