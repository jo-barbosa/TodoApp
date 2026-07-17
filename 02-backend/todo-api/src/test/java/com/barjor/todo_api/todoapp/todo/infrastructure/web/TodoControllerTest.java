package com.barjor.todo_api.todoapp.todo.infrastructure.web;

import com.barjor.todo_api.todoapp.todo.application.TodoService;
import com.barjor.todo_api.todoapp.todo.domain.Todo;
import com.barjor.todo_api.todoapp.todo.domain.exception.TodoNotFoundException;
import com.barjor.todo_api.todoapp.todo.domain.exception.TodoNotOwnedByUserException;
import com.barjor.todo_api.todoapp.todo.infrastructure.web.dto.CreateTodoRequest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TodoService todoService;

    @Test
    void createTodo_withValidInput_shouldReturnCreated() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID todoId = UUID.randomUUID();
        LocalDate dueDate = LocalDate.now();

        Todo mockTodo = new Todo(todoId, "Buy Milk", "2 cartons", false, dueDate, userId);
        when(todoService.createTodo(any(UUID.class), anyString(), any(), any())).thenReturn(mockTodo);

        CreateTodoRequest request = new CreateTodoRequest("Buy Milk", "2 cartons", dueDate);

        mockMvc.perform(post("/api/users/{userId}/todos", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(todoId.toString()))
                .andExpect(jsonPath("$.title").value("Buy Milk"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    void createTodo_withBlankTitle_shouldReturnBadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateTodoRequest request = new CreateTodoRequest("", "Desc", LocalDate.now());

        mockMvc.perform(post("/api/users/{userId}/todos", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void getTodo_whenNotOwned_shouldReturnForbidden() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID todoId = UUID.randomUUID();

        when(todoService.getTodoByIdAndUserId(todoId, userId))
                .thenThrow(new TodoNotOwnedByUserException(todoId, userId));

        mockMvc.perform(get("/api/users/{userId}/todos/{todoId}", userId, todoId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").value("Todo " + todoId + " is not owned by user " + userId));
    }

    @Test
    void getTodo_whenNotFound_shouldReturnNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID todoId = UUID.randomUUID();

        when(todoService.getTodoByIdAndUserId(todoId, userId))
                .thenThrow(new TodoNotFoundException(todoId));

        mockMvc.perform(get("/api/users/{userId}/todos/{todoId}", userId, todoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Todo Not Found"));
    }

    // Helper method to bypass mockito anyString() type constraints
    private String anyString() {
        return any(String.class);
    }
}
