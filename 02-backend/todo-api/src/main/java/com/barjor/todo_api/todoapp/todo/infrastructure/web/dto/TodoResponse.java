package com.barjor.todo_api.todoapp.todo.infrastructure.web.dto;

import com.barjor.todo_api.todoapp.todo.domain.Todo;

import java.time.LocalDate;
import java.util.UUID;

public record TodoResponse(
        UUID id,
        String title,
        String description,
        boolean completed,
        LocalDate dueDate,
        UUID userId
) {
    public static TodoResponse fromDomain(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getDueDate(),
                todo.getUserId()
        );
    }
}
