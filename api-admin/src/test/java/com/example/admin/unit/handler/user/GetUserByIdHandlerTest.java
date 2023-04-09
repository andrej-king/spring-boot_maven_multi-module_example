package com.example.admin.unit.handler.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.admin.dto.external.user.response.UserResponse;
import com.example.admin.handler.user.GetUserByIdHandlerImpl;
import com.example.admin.unit.AbstractUnitTest;
import com.example.common.domain.dto.external.common.request.IdRequest;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.detail.InvalidIdFormatException;
import com.example.security.exception.detail.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetUserByIdHandlerTest extends AbstractUnitTest {
    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private GetUserByIdHandlerImpl getUserByIdHandler;

    @Test
    public void test_findOneById_notFound() {
        Exception exception = assertThrows(Exception.class, () -> {
            getUserByIdHandler.handle(new IdRequest("1"));
        });

        String expectedMessage = ResourceNotFoundException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), ResourceNotFoundException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_findOneById_invalidIdFormat() {
        Exception exception = assertThrows(Exception.class, () -> {
            getUserByIdHandler.handle(new IdRequest("abc"));
        });

        String expectedMessage = InvalidIdFormatException.MSG;
        String actualMessage = exception.getMessage();

        assertSame(exception.getClass(), InvalidIdFormatException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_findOneById_success() {
        UserModel userModel = getTestUtil().getTestUser(RoleEnum.USER);
        when(mockUserRepository.findById(1L)).thenReturn(Optional.ofNullable(userModel));

        UserResponse userResponse = getUserByIdHandler.handle(new IdRequest("1"));

        verify(mockUserRepository, times(1)).findById(1L);

        assertNotNull(userModel);
        assertEquals(userResponse.uuid(), userModel.getUuid());
        assertEquals(userResponse.login(), userModel.getLogin());
        assertEquals(userResponse.email(), userModel.getEmail());
        assertEquals(userResponse.enabled(), userModel.getEnabled());
        assertEquals(userResponse.role().uuid(), userModel.getRole().getUuid());
        assertEquals(userResponse.role().name(), userModel.getRole().getName().name());
    }
}
