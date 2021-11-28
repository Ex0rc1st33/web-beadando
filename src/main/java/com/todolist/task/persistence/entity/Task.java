package com.todolist.task.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private final LocalDateTime creationDate = LocalDateTime.now();
    private Boolean isDone = false;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
