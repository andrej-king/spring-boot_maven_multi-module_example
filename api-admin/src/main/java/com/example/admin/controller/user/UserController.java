package com.example.admin.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.admin.controller.AdminController;
import com.example.admin.dto.external.user.response.UserResponse;
import com.example.admin.handler.user.DeleteUserByIdHandler;
import com.example.admin.handler.user.GetAllUsersHandler;
import com.example.admin.handler.user.GetUserByIdHandler;
import com.example.common.domain.constant.OpenApiConstant;
import com.example.common.domain.dto.external.common.request.IdRequest;

import java.util.List;

@Tag(name = "User", description = "Operations with user (ROLE_ADMIN)")
@RestController
@RequiredArgsConstructor
public class UserController extends AdminController {
    private final GetAllUsersHandler getAllUsersHandler;
    private final GetUserByIdHandler getUserByIdHandler;
    private final DeleteUserByIdHandler deleteUserByIdHandler;
    public static final String COLLECTION_OF_USERS_URL = "/users";
    public static final String USER_ITEM_URL = "/users/{id}";

    @Operation(summary = "Get list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(type = "array", implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "401", description = OpenApiConstant.UNAUTHORIZED, content = @Content),
            @ApiResponse(responseCode = "403", description = OpenApiConstant.FORBIDDEN, content = @Content)
    })
    @GetMapping(COLLECTION_OF_USERS_URL)
    public ResponseEntity<List<UserResponse>> getListOfUsers() {
        return new ResponseEntity<>(getAllUsersHandler.handle(), HttpStatus.OK);
    }

    @Operation(summary = "Get user by id")
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Id of user to be searched",
            schema = @Schema(implementation = IdRequest.class))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = OpenApiConstant.UNAUTHORIZED, content = @Content),
            @ApiResponse(responseCode = "403", description = OpenApiConstant.FORBIDDEN, content = @Content),
            @ApiResponse(responseCode = "404", description = OpenApiConstant.NOT_FOUND, content = @Content),
            @ApiResponse(responseCode = "422", description = OpenApiConstant.INVALID_FIELD_FORMAT, content = @Content)
    })
    @GetMapping(USER_ITEM_URL)
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        UserResponse response = getUserByIdHandler.handle(new IdRequest(id));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete user")
    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "Id of user to be deleted",
            schema = @Schema(implementation = IdRequest.class))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted", content = @Content),
            @ApiResponse(responseCode = "401", description = OpenApiConstant.UNAUTHORIZED, content = @Content),
            @ApiResponse(responseCode = "403", description = OpenApiConstant.FORBIDDEN, content = @Content),
            @ApiResponse(responseCode = "404", description = OpenApiConstant.NOT_FOUND, content = @Content),
            @ApiResponse(responseCode = "422", description = OpenApiConstant.INVALID_FIELD_FORMAT, content = @Content)
    })
    @DeleteMapping(USER_ITEM_URL)
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        deleteUserByIdHandler.handle(new IdRequest(id));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
