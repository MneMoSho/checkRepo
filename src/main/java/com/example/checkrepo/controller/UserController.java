package com.example.checkrepo.controller;

import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.exception.ErrorObject;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.TypeMissMatchException;
import com.example.checkrepo.service.UserServiceImpl;
import com.sun.net.httpserver.Authenticator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {
    private UserServiceImpl userService;

    @PostMapping()
    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
    })
    public void newUser(
            @Parameter(description = "User data transfer object", required = true)
            @RequestBody UserDto userAdd) {
        userService.createUser(userAdd);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID",
            description = "Retrieves user details by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TypeMissMatchException.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
    })
    public UserDto displayUser(
            @Parameter(description = "ID of the user to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping()
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user list"),
        @ApiResponse(responseCode = "404", description = "No users were loaded",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
    })
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID",
            description = "Removes a user from the system by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User successfully deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
    })
    public void deleteById(
            @Parameter(description = "ID of the user to delete", required = true, example = "1")
            @PathVariable Long id) {
        try {
            userService.deleteById(id);
        } catch (TypeMissMatchException e) {
            throw e;
        }
    }

    @Operation(summary = "Find users by flight destinations",
            description = "Retrieves users associated "
                    + "with flights matching the given destination list")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved matching users",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserDto.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid destination list"),
        @ApiResponse(responseCode = "404", description = "User was not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/byFlight")
    public List<UserDto> byRoute(
            @Parameter(description = "List of flight destinations to filter by", required = true)
            @RequestBody List<String> finalDests) {
        return userService.findByEndDest(finalDests);
    }
}