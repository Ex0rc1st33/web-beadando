package com.todolist.task.service;

import com.todolist.task.model.TaskDto;

import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskDto taskDto);

    TaskDto updateTask(TaskDto taskDto);

    void deleteTask(Long id);

    void deleteAllTasks();

    List<TaskDto> getTasks();

}
