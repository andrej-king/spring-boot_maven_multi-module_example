package com.example.admin.handler.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.admin.dto.external.user.response.RoleResponse;
import com.example.admin.dto.external.user.response.UserResponse;
import com.example.common.domain.dto.external.common.request.IdRequest;
import com.example.common.domain.util.StringUtil;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.detail.InvalidIdFormatException;
import com.example.security.exception.detail.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class GetUserByIdHandlerImpl implements GetUserByIdHandler {
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponse handle(@Valid final IdRequest idRequest) throws AppException {
        if (StringUtil.isInvalidLongType(idRequest.id())) {
            throw new InvalidIdFormatException();
        }

        UserModel userModel = userRepository
                .findById(Long.parseLong(idRequest.id()))
                .orElseThrow(ResourceNotFoundException::new);

        RoleResponse roleResponse = RoleResponse.builder()
                .id(userModel.getRole().getId())
                .uuid(userModel.getRole().getUuid())
                .name(userModel.getRole().getName().name())
                .build();

        return UserResponse.builder()
                .id(userModel.getId())
                .uuid(userModel.getUuid())
                .login(userModel.getLogin())
                .email(userModel.getEmail())
                .fullName(userModel.getFullName())
                .enabled(userModel.getEnabled())
                .role(roleResponse)
                .build();
    }
}
