package com.barjor.todo_api.todoapp.user.infrastructure.web.dto;

import com.barjor.todo_api.todoapp.user.domain.User;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
