package com.example.common.application.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import com.example.persistence.model.role.RoleEnum;

@RestController
abstract public class BaseController {
    public static final String AUTHORITY_ADMIN = "SCOPE_ROLE_" + RoleEnum.ADMIN.name();
    public static final String AUTHORITY_USER = "SCOPE_ROLE_" + RoleEnum.USER.name();

    /**
     * Set "application/json" header for each controller method
     */
    @ModelAttribute
    protected void setJsonResponseHeader(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    protected String getUsernameFromPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
