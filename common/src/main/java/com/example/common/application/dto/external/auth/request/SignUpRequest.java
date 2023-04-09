package com.example.common.application.dto.external.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @Schema(example = "john")
        @NotEmpty
        @Size(min = 3, max = 100)
        String login,

        @Schema(format = "email", example = "john@example.com")
        @Email
        @NotEmpty
        String email,

        @Schema(example = "somePassword", minLength = 8, maxLength = 32)
        @NotEmpty
        @Size(min = 8, max = 32)
        String password,

        @Schema(example = "John Doe", minLength = 3, maxLength = 100)
        @NotEmpty
        @Size(min = 3, max = 100)
        String fullName
) {
}
