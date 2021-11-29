package com.todolist.user.service;

import com.todolist.user.model.UserResponseDto;
import com.todolist.user.persistence.entity.User;

public interface UserService {

    String signIn(String username, String password);

    String signUp(User user);

    UserResponseDto whoAmI(String username);

    String signOut();

}
