package com.example.common.application.dto.external.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.UUID;

public record RefreshTokenRequest(
        @Schema(example = "21b8fef9-6417-4ceb-9f4b-f6a025ed057f", format = "uuid")
        @UUID
        String refreshToken
) {
}
