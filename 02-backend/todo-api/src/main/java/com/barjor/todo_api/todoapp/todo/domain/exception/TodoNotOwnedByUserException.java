package com.barjor.todo_api.todoapp.todo.domain.exception;

import java.util.UUID;

public class TodoNotOwnedByUserException extends RuntimeException {
    public TodoNotOwnedByUserException(UUID todoId, UUID userId) {
        super("Todo " + todoId + " is not owned by user " + userId);
    }
}
