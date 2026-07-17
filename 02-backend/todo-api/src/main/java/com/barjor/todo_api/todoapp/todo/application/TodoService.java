package com.barjor.todo_api.todoapp.todo.application;

import com.barjor.todo_api.todoapp.todo.domain.Todo;
import com.barjor.todo_api.todoapp.todo.domain.TodoRepository;
import com.barjor.todo_api.todoapp.todo.domain.exception.TodoNotFoundException;
import com.barjor.todo_api.todoapp.todo.domain.exception.TodoNotOwnedByUserException;
import com.barjor.todo_api.todoapp.user.domain.UserRepository;
import com.barjor.todo_api.todoapp.user.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public Todo createTodo(UUID userId, String title, String description, LocalDate dueDate) {
        verifyUserExists(userId);
        Todo todo = new Todo(UUID.randomUUID(), title, description, false, dueDate, userId);
        return todoRepository.save(todo);
    }

    public List<Todo> getTodosByUserId(UUID userId) {
        verifyUserExists(userId);
        return todoRepository.findByUserId(userId);
    }

    public Todo getTodoByIdAndUserId(UUID todoId, UUID userId) {
        verifyUserExists(userId);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(todoId));
        
        if (!todo.getUserId().equals(userId)) {
            throw new TodoNotOwnedByUserException(todoId, userId);
        }
        return todo;
    }

    public Todo updateTodo(UUID todoId, UUID userId, String title, String description, LocalDate dueDate, boolean completed) {
        verifyUserExists(userId);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(todoId));

        if (!todo.getUserId().equals(userId)) {
            throw new TodoNotOwnedByUserException(todoId, userId);
        }

        Todo updatedTodo = todo.update(title, description, dueDate, completed);
        return todoRepository.save(updatedTodo);
    }

    public void deleteTodo(UUID todoId, UUID userId) {
        verifyUserExists(userId);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoNotFoundException(todoId));

        if (!todo.getUserId().equals(userId)) {
            throw new TodoNotOwnedByUserException(todoId, userId);
        }

        todoRepository.delete(todoId);
    }

    private void verifyUserExists(UUID userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException(userId);
        }
    }
}
