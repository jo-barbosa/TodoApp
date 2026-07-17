package com.barjor.todo_api.todoapp.todo.application;

import com.barjor.todo_api.todoapp.todo.domain.Todo;
import com.barjor.todo_api.todoapp.todo.domain.TodoRepository;
import com.barjor.todo_api.todoapp.todo.domain.exception.TodoNotFoundException;
import com.barjor.todo_api.todoapp.todo.domain.exception.TodoNotOwnedByUserException;
import com.barjor.todo_api.todoapp.user.domain.User;
import com.barjor.todo_api.todoapp.user.domain.UserRepository;
import com.barjor.todo_api.todoapp.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(todoRepository, userRepository);
    }

    @Test
    void createTodo_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.createTodo(userId, "Desc", LocalDate.now()))
                .isInstanceOf(UserNotFoundException.class);

        verify(todoRepository, never()).save(any());
    }

    @Test
    void createTodo_whenUserExists_shouldSaveTodo() {
        UUID userId = UUID.randomUUID();
        User mockUser = new User(userId, "John", "john@example.com", "password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        Todo mockSavedTodo = new Todo(UUID.randomUUID(), "Desc", false, LocalDate.now(), userId);
        when(todoRepository.save(any(Todo.class))).thenReturn(mockSavedTodo);

        Todo result = todoService.createTodo(userId, "Desc", LocalDate.now());

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Desc");
        assertThat(result.getUserId()).isEqualTo(userId);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void getTodoByIdAndUserId_whenNotOwned_shouldThrowTodoNotOwnedByUserException() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID todoId = UUID.randomUUID();

        User mockUser = new User(userId, "John", "john@example.com", "password");
        Todo mockTodo = new Todo(todoId, "Desc", false, LocalDate.now(), otherUserId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(mockTodo));

        assertThatThrownBy(() -> todoService.getTodoByIdAndUserId(todoId, userId))
                .isInstanceOf(TodoNotOwnedByUserException.class);
    }
}
