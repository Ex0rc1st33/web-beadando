package com.todolist.task.service;

import com.todolist.task.model.TaskDto;
import com.todolist.task.model.TaskResponseDto;
import com.todolist.user.model.UserDto;

import java.util.List;

public interface TaskService {

    TaskResponseDto createTask(TaskDto taskDto);

    TaskResponseDto updateTask(TaskDto taskDto);

    void deleteTaskByUser(TaskDto taskDto);

    void deleteAllTasksByUser(String username);

    List<TaskResponseDto> getTasksByUser(String username);

}
