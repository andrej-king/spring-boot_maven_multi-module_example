package com.example.admin.unit.handler.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import com.example.admin.dto.external.user.response.UserResponse;
import com.example.admin.handler.user.GetAllUsersHandlerImpl;
import com.example.admin.unit.AbstractUnitTest;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllUsersHandlerTest extends AbstractUnitTest {
    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private GetAllUsersHandlerImpl getAllUsersHandler;

    @Test
    public void test_findAll_empty() {
        List<UserModel> userModels = new ArrayList<>();

        when(mockUserRepository.findAll(Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(userModels);

        List<UserResponse> userResponses = getAllUsersHandler.handle();

        verify(mockUserRepository, times(1))
                .findAll(Sort.by(Sort.Direction.DESC, "id"));

        assertEquals(0, userResponses.size());
    }

    @Test
    public void test_findAll_success() {
        List<UserModel> userModels = new ArrayList<>();
        userModels.add(getTestUtil().getTestUser(RoleEnum.USER));

        when(mockUserRepository.findAll(Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(userModels);

        List<UserResponse> userResponses = getAllUsersHandler.handle();

        verify(mockUserRepository, times(1))
                .findAll(Sort.by(Sort.Direction.DESC, "id"));

        assertEquals(1, userResponses.size());
        UserModel userModel = userModels.get(0);
        UserResponse userResponse = userResponses.get(0);

        assertEquals(userResponse.uuid(), userModel.getUuid());
        assertEquals(userResponse.login(), userModel.getLogin());
        assertEquals(userResponse.email(), userModel.getEmail());
        assertEquals(userResponse.enabled(), userModel.getEnabled());
        assertEquals(userResponse.role().uuid(), userModel.getRole().getUuid());
        assertEquals(userResponse.role().name(), userModel.getRole().getName().name());
    }
}
