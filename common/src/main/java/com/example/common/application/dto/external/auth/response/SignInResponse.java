package com.example.common.application.dto.external.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInResponse(
        @Schema(example = "c945da34-5273-4607-85df-f9f335b7fbb8", format = "uuid")
        String refreshToken,
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String accessToken,

        @Schema(example = "Bearer")
        String type
) {
    public SignInResponse(String refreshToken, String accessToken) {
        this(refreshToken, accessToken, "Bearer");
    }
}
