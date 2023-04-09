package com.example.common.application.handler.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.role.RoleModel;
import com.example.persistence.repository.RoleRepository;
import com.example.security.exception.detail.ResourceNotFoundException;


/**
 * Methods for work with roles
 */
@Service
@RequiredArgsConstructor
public class RoleHandlerImpl implements RoleHandler {
    private final RoleRepository roleRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public RoleModel getRoleByNameOrCreate(RoleEnum name) {
        if (!roleRepository.existsByName(name)) {
            return roleRepository.saveAndFlush(new RoleModel(name));
        }

        return roleRepository
                .findByName(name)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
