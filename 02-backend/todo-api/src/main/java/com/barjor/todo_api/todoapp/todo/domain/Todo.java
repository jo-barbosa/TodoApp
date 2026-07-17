package com.barjor.todo_api.todoapp.todo.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public final class Todo {
    private final UUID id;
    private final String description;
    private final boolean completed;
    private final LocalDate dueDate;
    private final UUID userId;

    public Todo(UUID id, String description, boolean completed, LocalDate dueDate, UUID userId) {
        this.id = Objects.requireNonNull(id, "Todo ID cannot be null");
        this.userId = Objects.requireNonNull(userId, "UserId cannot be null");
        
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Todo description cannot be null or blank");
        }
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public Todo complete() {
        return new Todo(id, description, true, dueDate, userId);
    }

    public Todo uncomplete() {
        return new Todo(id, description, false, dueDate, userId);
    }

    public Todo update(String newDescription, LocalDate newDueDate, boolean completed) {
        return new Todo(id, newDescription, completed, newDueDate, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
