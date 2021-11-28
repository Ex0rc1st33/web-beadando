package com.todolist.user.service;

import com.todolist.user.persistence.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    String signin(String username, String password);

    String signup(User user);

    User whoami(HttpServletRequest req);

    String refresh(String username);

}
