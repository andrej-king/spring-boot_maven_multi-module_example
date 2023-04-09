package com.example.main.dto.external.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @Schema(example = "somePassword", minLength = 8, maxLength = 32)
        @NotEmpty
        @Size(min = 8, max = 32)
        String oldPassword,

        @Schema(example = "someNewPassword", minLength = 8, maxLength = 32)
        @NotEmpty
        @Size(min = 8, max = 32)
        String newPassword
) {
}
