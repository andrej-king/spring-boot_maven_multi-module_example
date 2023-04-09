package com.example.common.application.handler.role;

import com.example.common.application.handler.BaseHandler;
import com.example.persistence.model.role.RoleEnum;
import com.example.persistence.model.role.RoleModel;

public interface RoleHandler extends BaseHandler {
    /**
     * Get role by name (create if not exists)
     */
    RoleModel getRoleByNameOrCreate(RoleEnum name);
}
