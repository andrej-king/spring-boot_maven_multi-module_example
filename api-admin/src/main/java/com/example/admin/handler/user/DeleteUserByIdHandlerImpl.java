package com.example.admin.handler.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.common.domain.dto.external.common.request.IdRequest;
import com.example.common.domain.util.StringUtil;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.user.UserModel;
import com.example.persistence.repository.UserRepository;
import com.example.security.exception.AppException;
import com.example.security.exception.detail.DeleteAdminException;
import com.example.security.exception.detail.InvalidIdFormatException;
import com.example.security.exception.detail.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class DeleteUserByIdHandlerImpl implements DeleteUserByIdHandler {
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@Valid final IdRequest idRequest) throws AppException {
        if (StringUtil.isInvalidLongType(idRequest.id())) {
            throw new InvalidIdFormatException();
        }

        /* Search user by id */
        UserModel userEntity = userRepository
                .findById(Long.parseLong(idRequest.id()))
                .orElseThrow(ResourceNotFoundException::new);

        /* Check if try to delete ADMIN */
        if (userEntity.getRole().getName().equals(RoleEnum.ADMIN)) {
            throw new DeleteAdminException();
        }

        userRepository.delete(userEntity);
    }
}
