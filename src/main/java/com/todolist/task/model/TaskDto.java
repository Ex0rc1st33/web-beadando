package com.todolist.task.model;

import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class TaskDto {

    private final Long id;
    private final String title;
    private final String description;
    private final String creationDate;
    private final Boolean isDone;

}
