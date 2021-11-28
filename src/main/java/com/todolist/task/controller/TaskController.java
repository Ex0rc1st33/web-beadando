package com.todolist.task.controller;

import com.todolist.task.model.FormData;
import com.todolist.task.model.TaskDto;
import com.todolist.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/api/create")
    public ResponseEntity<?> createTask(@RequestBody FormData formData) {
        TaskDto taskDto = TaskDto.builder()
                .title(formData.getTitle())
                .description(formData.getDescription())
                .creationDate(null)
                .isDone(false)
                .build();
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @PostMapping("/api/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskDto));
    }

    @PostMapping("/api/delete")
    public ResponseEntity<?> deleteTask(@RequestBody String id) {
        taskService.deleteTask(Long.parseLong(id.substring(0, id.length() - 1)));
        return ResponseEntity.ok("Task deleted");
    }

    @GetMapping("/api/list")
    public ResponseEntity<?> listTasks() {
        return ResponseEntity.ok(taskService.getTasks());
    }

    @PostMapping("/api/delete_all")
    public ResponseEntity<?> deleteAllTasks() {
        taskService.deleteAllTasks();
        return ResponseEntity.ok("Tasks deleted");
    }

}
