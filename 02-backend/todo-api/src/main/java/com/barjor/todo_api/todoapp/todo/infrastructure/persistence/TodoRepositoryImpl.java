package com.barjor.todo_api.todoapp.todo.infrastructure.persistence;

import com.barjor.todo_api.todoapp.todo.domain.Todo;
import com.barjor.todo_api.todoapp.todo.domain.TodoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final JpaTodoRepository jpaTodoRepository;

    public TodoRepositoryImpl(JpaTodoRepository jpaTodoRepository) {
        this.jpaTodoRepository = jpaTodoRepository;
    }

    @Override
    public Todo save(Todo todo) {
        TodoEntity entity = toEntity(todo);
        TodoEntity savedEntity = jpaTodoRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        return jpaTodoRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Todo> findByUserId(UUID userId) {
        return jpaTodoRepository.findByUserId(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        jpaTodoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaTodoRepository.existsById(id);
    }

    private TodoEntity toEntity(Todo todo) {
        return new TodoEntity(
                todo.getId(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getDueDate(),
                todo.getUserId()
        );
    }

    private Todo toDomain(TodoEntity entity) {
        return new Todo(
                entity.getId(),
                entity.getDescription(),
                entity.isCompleted(),
                entity.getDueDate(),
                entity.getUserId()
        );
    }
}
