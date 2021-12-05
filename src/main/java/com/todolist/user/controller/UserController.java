package com.todolist.user.controller;

import com.todolist.user.exception.CustomException;
import com.todolist.user.model.FormData;
import com.todolist.user.model.UserDto;
import com.todolist.user.model.UserResponseDto;
import com.todolist.user.persistence.entity.User;
import com.todolist.user.service.UserService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

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
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<?> login(@RequestBody FormData formData, HttpServletResponse response) {
        String token = userService.signIn(formData.getUsername(), formData.getPassword());
        Cookie cookie = new Cookie("jwt", token);
        setCookie(cookie, 300);
        response.addCookie(cookie);
        return ResponseEntity.ok(token);
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
    public UserResponseDto whoAmI(HttpServletRequest request) {
        Optional<String> jwt = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt")).map(Cookie::getValue).findFirst();
        if (jwt.isEmpty()) {
            throw new CustomException("Expired cookie", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return modelMapper.map(userService.whoAmI(jwt.get()), UserResponseDto.class);
    }

    @GetMapping("/api/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> refresh(HttpServletRequest req, HttpServletResponse response) {
        String newToken = userService.refresh(req.getRemoteUser());
        Cookie cookie = new Cookie("jwt", newToken);
        setCookie(cookie, 300);
        response.addCookie(cookie);
        return ResponseEntity.ok(newToken);
    }

    @GetMapping("/api/logout")
    @ApiOperation(value = "${UserController.logout}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        setCookie(cookie, 0);
        response.addCookie(cookie);
        return userService.signOut();
    }

    private void setCookie(Cookie cookie, int age) {
        cookie.setMaxAge(age);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
    }

}
