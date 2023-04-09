package com.example.admin.dto.external.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RoleResponse(
        @Schema(example = "1")
        Long id,

        @Schema(example = "34fe8bbb-712a-4c1e-8ecf-286799dcf20f")
        UUID uuid,

        @Schema(example = "USER")
        String name
) {
}
