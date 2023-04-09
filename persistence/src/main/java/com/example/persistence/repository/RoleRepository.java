package com.example.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.role.RoleModel;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    /**
     * Find role by name (internal use)
     */
    Optional<RoleModel> findByName(RoleEnum name);

    /**
     * Check if role with name exists
     */
    Boolean existsByName(RoleEnum name);
}
