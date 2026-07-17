package com.barjor.todo_api.todoapp.todo.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaTodoRepository extends JpaRepository<TodoEntity, UUID> {
    List<TodoEntity> findByUserId(UUID userId);
}
