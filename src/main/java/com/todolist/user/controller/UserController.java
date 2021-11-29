package com.todolist.user.controller;

import com.todolist.user.model.FormData;
import com.todolist.user.model.UserDto;
import com.todolist.user.model.UserResponseDto;
import com.todolist.user.persistence.entity.User;
import com.todolist.user.service.UserService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Api(tags = "users")
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/api/login")
    @ApiOperation(value = "${UserController.login}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String login(@RequestBody FormData formData) {
        return userService.signIn(formData.getUsername(), formData.getPassword());
    }

    @PostMapping("/api/register")
    @ApiOperation(value = "${UserController.register}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public String register(@RequestBody UserDto userDto) {
        return userService.signUp(modelMapper.map(userDto, User.class));
    }

    @GetMapping("/api/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${UserController.whoAmI}", response = UserResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDto whoAmI(Principal user) {
        return modelMapper.map(userService.whoAmI(user.getName()), UserResponseDto.class);
    }

    @GetMapping("/api/logout")
    @ApiOperation(value = "${UserController.logout}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String logout() {
        return userService.signOut();
    }

}
