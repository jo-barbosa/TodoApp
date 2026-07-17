package com.barjor.todo_api.todoapp.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);
    java.util.Optional<UserEntity> findByEmail(String email);
}
