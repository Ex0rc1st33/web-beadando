package com.todolist.task.service.impl;

import com.todolist.task.model.TaskDto;
import com.todolist.task.model.TaskResponseDto;
import com.todolist.task.persistence.entity.Task;
import com.todolist.task.persistence.repository.TaskRepository;
import com.todolist.task.service.TaskService;
import com.todolist.user.model.UserDto;
import com.todolist.user.persistence.entity.User;
import com.todolist.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskResponseDto createTask(TaskDto taskDto) {
        Task task = new Task(
                taskDto.getTitle(),
                taskDto.getDescription(),
                userRepository.findByUsername(taskDto.getUsername())
        );
        return convertEntityToDto(taskRepository.save(task));
    }

    @Override
    public TaskResponseDto updateTask(TaskDto taskDto) {
        Task task = taskRepository.findById(taskDto.getId()).orElseThrow(() -> new IllegalArgumentException("Task does not exist"));
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setIsDone(taskDto.getIsDone());
        taskRepository.save(task);
        return convertEntityToDto(task);
    }

    @Override
    public void deleteTaskByUser(TaskDto taskDto) {
        User user = userRepository.findByUsername(taskDto.getUsername());
        taskRepository.deleteByIdAndUser(taskDto.getId(), user);
    }

    @Override
    public void deleteAllTasksByUser(String username) {
        User user = userRepository.findByUsername(username);
        taskRepository.deleteAllByUser(user);
    }

    @Override
    public List<TaskResponseDto> getTasksByUser(String username) {
        User user = userRepository.findByUsername(username);
        return taskRepository.findAllByUser(user)
                .stream()
                .map(this::convertEntityToDto)
                .sorted(Comparator.comparing(TaskResponseDto::getCreationDate).reversed())
                .collect(Collectors.toList());
    }

    private TaskResponseDto convertEntityToDto(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .creationDate(task.getCreationDate().format(formatter))
                .isDone(task.getIsDone())
                .build();
    }

}
