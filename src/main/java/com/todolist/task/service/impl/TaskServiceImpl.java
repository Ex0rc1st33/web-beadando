package com.todolist.task.service.impl;

import com.todolist.task.model.TaskDto;
import com.todolist.task.persistence.entity.Task;
import com.todolist.task.persistence.repository.TaskRepository;
import com.todolist.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = new Task(
                taskDto.getTitle(),
                taskDto.getDescription()
        );
        return convertEntityToDto(taskRepository.save(task));
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        Task task = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new IllegalArgumentException("Task does not exist"));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setIsDone(taskDto.getIsDone());
        taskRepository.save(task);
        return convertEntityToDto(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    @Override
    public List<TaskDto> getTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .sorted(Comparator.comparing(TaskDto::getCreationDate).reversed())
                .collect(Collectors.toList());
    }

    private TaskDto convertEntityToDto(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .creationDate(task.getCreationDate().format(formatter))
                .isDone(task.getIsDone())
                .build();
    }

}
