package com.barjor.todo_api.todoapp.user.infrastructure.web;

import com.barjor.todo_api.todoapp.user.application.UserService;
import com.barjor.todo_api.todoapp.user.domain.User;
import com.barjor.todo_api.todoapp.user.infrastructure.web.dto.CreateUserRequest;
import com.barjor.todo_api.todoapp.user.infrastructure.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User createdUser = userService.createUser(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(createdUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(UserResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(users);
    }
}
