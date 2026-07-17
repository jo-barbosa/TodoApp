package com.barjor.todo_api.todoapp.todo.domain.exception;

import java.util.UUID;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(UUID id) {
        super("Todo not found with ID: " + id);
    }
}
