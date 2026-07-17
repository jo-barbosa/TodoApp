package com.barjor.todo_api.todoapp.todo.infrastructure.web;

import com.barjor.todo_api.todoapp.todo.application.TodoService;
import com.barjor.todo_api.todoapp.todo.domain.Todo;
import com.barjor.todo_api.todoapp.todo.infrastructure.web.dto.CreateTodoRequest;
import com.barjor.todo_api.todoapp.todo.infrastructure.web.dto.TodoResponse;
import com.barjor.todo_api.todoapp.todo.infrastructure.web.dto.UpdateTodoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @PathVariable UUID userId,
            @Valid @RequestBody CreateTodoRequest request
    ) {
        Todo todo = todoService.createTodo(userId, request.getDescription(), request.getDueDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(TodoResponse.fromDomain(todo));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos(@PathVariable UUID userId) {
        List<TodoResponse> todos = todoService.getTodosByUserId(userId).stream()
                .map(TodoResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(
            @PathVariable UUID userId,
            @PathVariable UUID todoId
    ) {
        Todo todo = todoService.getTodoByIdAndUserId(todoId, userId);
        return ResponseEntity.ok(TodoResponse.fromDomain(todo));
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable UUID userId,
            @PathVariable UUID todoId,
            @Valid @RequestBody UpdateTodoRequest request
    ) {
        Todo todo = todoService.updateTodo(
                todoId,
                userId,
                request.getDescription(),
                request.getDueDate(),
                request.getCompleted()
        );
        return ResponseEntity.ok(TodoResponse.fromDomain(todo));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable UUID userId,
            @PathVariable UUID todoId
    ) {
        todoService.deleteTodo(todoId, userId);
        return ResponseEntity.noContent().build();
    }
}
