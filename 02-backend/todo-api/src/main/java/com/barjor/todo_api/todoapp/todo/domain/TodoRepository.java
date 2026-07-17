package com.barjor.todo_api.todoapp.todo.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(UUID id);
    List<Todo> findByUserId(UUID userId);
    void delete(UUID id);
    boolean existsById(UUID id);
}
