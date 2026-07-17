package com.barjor.todo_api.todoapp.user.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists");
    }
}
