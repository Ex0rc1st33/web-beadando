package com.todolist.task.controller;

import com.todolist.task.model.FormData;
import com.todolist.task.model.TaskDto;
import com.todolist.task.service.TaskService;
import com.todolist.user.exception.CustomException;
import com.todolist.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RestController
public class TaskController {

    private final TaskService taskService;

    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/api/create")
    public ResponseEntity<?> createTask(@RequestBody FormData formData, HttpServletRequest request) throws IOException {
        if (isValidToken(request)) {
            TaskDto taskDto = TaskDto.builder()
                    .title(formData.getTitle())
                    .description(formData.getDescription())
                    .creationDate(null)
                    .isDone(false)
                    .username(formData.getUsername())
                    .build();
            return ResponseEntity.ok(taskService.createTask(taskDto));
        }
        return ResponseEntity.status(500).body("Expired or invalid JWT token");
    }

    @PostMapping("/api/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        if (isValidToken(request)) {
            return ResponseEntity.ok(taskService.updateTask(taskDto));
        }
        return ResponseEntity.status(500).body("Expired or invalid JWT token");
    }

    @PostMapping("/api/delete")
    public ResponseEntity<?> deleteTaskByUser(@RequestBody TaskDto taskDto, HttpServletRequest request) {
        if (isValidToken(request)) {
            taskService.deleteTaskByUser(taskDto);
            return ResponseEntity.ok("Task deleted");
        }
        return ResponseEntity.status(500).body("Expired or invalid JWT token");
    }

    @PostMapping("/api/delete_all")
    public ResponseEntity<?> deleteAllTasksByUser(@RequestBody String username, HttpServletRequest request) {
        if (isValidToken(request)) {
            taskService.deleteAllTasksByUser(username.substring(0, username.length() - 1));
            return ResponseEntity.ok("Tasks deleted");
        }
        return ResponseEntity.status(500).body("Expired or invalid JWT token");
    }

    @PostMapping("/api/list")
    public ResponseEntity<?> listTasksByUser(@RequestBody String username, HttpServletRequest request) {
        if (isValidToken(request)) {
            return ResponseEntity.ok(taskService.getTasksByUser(username.substring(0, username.length() - 1)));
        }
        return ResponseEntity.status(500).body("Expired or invalid JWT token");
    }

    private boolean isValidToken(HttpServletRequest request) {
        Optional<String> jwt = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt")).map(Cookie::getValue).findFirst();
        if (jwt.isEmpty()) {
            throw new CustomException("Expired cookie", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return userService.validateToken(jwt.get());
    }

}
