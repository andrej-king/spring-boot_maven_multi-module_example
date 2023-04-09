package com.example.admin.handler.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.admin.dto.external.user.response.RoleResponse;
import com.example.admin.dto.external.user.response.UserResponse;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersHandlerImpl implements GetAllUsersHandler {
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserResponse> handle() {
        List<UserResponse> usersResponse = new ArrayList<>();

        List<UserModel> userModels = userRepository
                .findAll(Sort.by(Sort.Direction.DESC, "id"));

        for (UserModel userModel : userModels) {
            RoleResponse roleResponse = RoleResponse.builder()
                    .id(userModel.getRole().getId())
                    .uuid(userModel.getRole().getUuid())
                    .name(userModel.getRole().getName().name())
                    .build();

            UserResponse userResponse = UserResponse.builder()
                    .id(userModel.getId())
                    .uuid(userModel.getUuid())
                    .login(userModel.getLogin())
                    .email(userModel.getEmail())
                    .fullName(userModel.getFullName())
                    .enabled(userModel.getEnabled())
                    .role(roleResponse)
                    .build();

            usersResponse.add(userResponse);
        }

        return usersResponse;
    }
}
