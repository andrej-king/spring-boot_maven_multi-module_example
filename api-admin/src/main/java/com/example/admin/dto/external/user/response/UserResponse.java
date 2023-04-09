package com.example.admin.dto.external.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        @Schema(example = "1")
        Long id,

        @Schema(example = "d27336e0-27b5-46eb-9cd2-60d975882e61")
        UUID uuid,

        @Schema(example = "john")
        String login,

        @Schema(format = "email", example = "john@example.com")
        String email,

        @Schema(example = "John Doe")
        String fullName,

        @Schema(example = "true")
        Boolean enabled,
        RoleResponse role
) {
}
