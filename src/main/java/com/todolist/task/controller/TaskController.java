package com.todolist.task.controller;

import com.todolist.task.model.FormData;
import com.todolist.task.model.TaskDto;
import com.todolist.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/api/create")
    public ResponseEntity<?> createTask(@RequestBody FormData formData) throws IOException {
        TaskDto taskDto = TaskDto.builder()
                .title(formData.getTitle())
                .description(formData.getDescription())
                .creationDate(null)
                .isDone(false)
                .username(formData.getUsername())
                .build();
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @PostMapping("/api/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskDto));
    }

    @PostMapping("/api/delete")
    public ResponseEntity<?> deleteTaskByUser(@RequestBody TaskDto taskDto) {
        taskService.deleteTaskByUser(taskDto);
        return ResponseEntity.ok("Task deleted");
    }

    @PostMapping("/api/delete_all")
    public ResponseEntity<?> deleteAllTasksByUser(@RequestBody String username) {
        taskService.deleteAllTasksByUser(username.substring(0, username.length() - 1));
        return ResponseEntity.ok("Tasks deleted");
    }

    @PostMapping("/api/list")
    public ResponseEntity<?> listTasksByUser(@RequestBody String username) {
        return ResponseEntity.ok(taskService.getTasksByUser(username.substring(0, username.length() - 1)));
    }

}
