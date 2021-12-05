package com.todolist.user.service;

import com.todolist.user.model.UserResponseDto;
import com.todolist.user.persistence.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    String signIn(String username, String password);

    String signUp(User user);

    UserResponseDto whoAmI(String token);

    boolean validateToken(String token);

    String refresh(String username);

    String signOut();

}
