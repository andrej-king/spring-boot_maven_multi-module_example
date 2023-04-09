package com.example.common.domain.dto.external.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Schema(type = "integer", format = "int64", example = "1")
public record IdRequest(
        @NotEmpty
        @Size(min = 1)
        String id
) {
}
