package com.barjor.todo_api.todoapp.user.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
