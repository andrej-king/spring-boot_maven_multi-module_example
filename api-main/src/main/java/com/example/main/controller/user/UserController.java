package com.example.main.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.common.domain.constant.OpenApiConstant;
import com.example.main.controller.MainAuthController;
import com.example.main.dto.external.user.request.UpdatePasswordRequest;
import com.example.main.handler.user.UpdatePasswordHandler;

@Tag(name = "User", description = "Operations with user (ROLE_USER)")
@RestController
@RequiredArgsConstructor
public class UserController extends MainAuthController {
    private final UpdatePasswordHandler updatePasswordHandler;

    private final String INVALID_FIELD_FORMAT_OR_PASSWORD = OpenApiConstant.INVALID_FIELD_FORMAT
            + " | Invalid old password";

    public static final String USER_UPDATE_PASSWORD_URL = "/users/update/password";

    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = OpenApiConstant.UNAUTHORIZED, content = @Content),
            @ApiResponse(responseCode = "403", description = OpenApiConstant.FORBIDDEN, content = @Content),
            @ApiResponse(responseCode = "404", description = OpenApiConstant.NOT_FOUND, content = @Content),
            @ApiResponse(responseCode = "422", description = INVALID_FIELD_FORMAT_OR_PASSWORD, content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = UpdatePasswordRequest.class)))
    @PutMapping(USER_UPDATE_PASSWORD_URL)
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePassword) {
        String username = getUsernameFromPrincipal();
        updatePasswordHandler.handle(updatePassword, username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
