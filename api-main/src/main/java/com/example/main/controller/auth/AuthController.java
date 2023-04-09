package com.example.main.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.common.application.controller.BaseController;
import com.example.common.application.dto.external.auth.request.RefreshTokenRequest;
import com.example.common.application.dto.external.auth.request.SignInRequest;
import com.example.common.application.dto.external.auth.request.SignUpRequest;
import com.example.common.application.dto.external.auth.response.SignInResponse;
import com.example.common.application.dto.external.auth.response.SignUpResponse;
import com.example.common.application.handler.auth.refresh.RefreshTokenHandler;
import com.example.common.application.handler.auth.signin.SignInHandler;
import com.example.common.application.handler.auth.signup.SignUpHandler;
import com.example.common.domain.constant.OpenApiConstant;
import com.example.persistence.model.role.RoleEnum;
import com.example.security.exception.detail.LoginInfoException;
import com.example.security.exception.detail.RefreshTokenException;

@Tag(name = "Auth", description = "SignIn, SignUp, RefreshToken operations")
@SecurityRequirement(name = "api")
@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final SignInHandler signInHandler;
    private final SignUpHandler signUpHandler;
    private final RefreshTokenHandler refreshTokenHandler;

    public static final String SIGN_UP_URL = "/auth/signup";
    public static final String SIGN_IN_URL = "/auth/signin";
    public static final String REFRESH_TOKEN_URL = "/auth/refresh";

    private final String INVALID_FIELD_FORMAT_OR_TOKEN =
            OpenApiConstant.INVALID_FIELD_FORMAT + " | " + RefreshTokenException.MSG;

    public static final String AUTH_FIELD_OR_EXISTS =
            OpenApiConstant.INVALID_FIELD_FORMAT + " | Login or email already exists";

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sign up success", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SignUpResponse.class))}),
            @ApiResponse(responseCode = "422", description = AUTH_FIELD_OR_EXISTS, content = @Content)})
    @PostMapping(SIGN_UP_URL)
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = signUpHandler.handle(signUpRequest);

        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a bearer token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login success", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SignInResponse.class))}),
            @ApiResponse(responseCode = "401", description = LoginInfoException.MSG, content = @Content),
            @ApiResponse(responseCode = "422", description = OpenApiConstant.INVALID_FIELD_FORMAT, content = @Content)})
    @PostMapping(SIGN_IN_URL)
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody final SignInRequest signInRequest) {
        SignInResponse signInResponse = signInHandler.handle(signInRequest, RoleEnum.USER);

        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }

    @Operation(summary = "Refresh bearer token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SignInResponse.class))}),
            @ApiResponse(responseCode = "422", description = INVALID_FIELD_FORMAT_OR_TOKEN, content = @Content)})
    @PostMapping(REFRESH_TOKEN_URL)
    public ResponseEntity<SignInResponse> refreshToken(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        SignInResponse signInResponse = refreshTokenHandler.handle(refreshTokenRequest, RoleEnum.USER);

        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }
}
