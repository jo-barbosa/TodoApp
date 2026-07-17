package com.barjor.todo_api.todoapp.user.infrastructure.web;

import com.barjor.todo_api.todoapp.security.JwtConfigurationProperties;
import com.barjor.todo_api.todoapp.security.SecurityConfiguration;
import com.barjor.todo_api.todoapp.user.application.UserService;
import com.barjor.todo_api.todoapp.user.domain.User;
import com.barjor.todo_api.todoapp.user.domain.exception.EmailAlreadyExistsException;
import com.barjor.todo_api.todoapp.user.infrastructure.web.dto.CreateUserRequest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = UserController.class,
    properties = {
        "jwt.secret-key=dGhpcy1pcy1hLXNlY3JldC1rZXktMzItYnl0ZXMtYW5kLW11c3QtYmUtMjU2LWJpdHMtbG9uZw==",
        "jwt.expiration-seconds=86400"
    }
)
@Import({SecurityConfiguration.class, JwtConfigurationProperties.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser_withValidInput_shouldReturnCreated() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = new User(userId, "John Doe", "john@example.com", "password");
        when(userService.createUser("John Doe", "john@example.com", "password")).thenReturn(mockUser);

        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", "password");

        mockMvc.perform(post("/api/users")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createUser_withInvalidEmail_shouldReturnBadRequest() throws Exception {
        CreateUserRequest request = new CreateUserRequest("John Doe", "invalid-email", "password");

        mockMvc.perform(post("/api/users")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }

    @Test
    void createUser_whenEmailExists_shouldReturnConflict() throws Exception {
        when(userService.createUser(anyString(), anyString(), anyString()))
                .thenThrow(new EmailAlreadyExistsException("john@example.com"));

        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", "password");

        mockMvc.perform(post("/api/users")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("User with email 'john@example.com' already exists"));
    }
}
