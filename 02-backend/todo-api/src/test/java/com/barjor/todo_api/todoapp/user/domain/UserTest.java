package com.barjor.todo_api.todoapp.user.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void constructor_withValidFields_shouldCreateUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "João Barbosa", "joao@example.com", "password");

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("João Barbosa");
        assertThat(user.getEmail()).isEqualTo("joao@example.com");
        assertThat(user.getPassword()).isEqualTo("password");
    }

    @Test
    void constructor_withNullId_shouldThrowException() {
        assertThatThrownBy(() -> new User(null, "João", "joao@example.com", "password"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void constructor_withBlankName_shouldThrowException() {
        assertThatThrownBy(() -> new User(UUID.randomUUID(), "   ", "joao@example.com", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User name cannot be null or blank");
    }

    @Test
    void constructor_withInvalidEmail_shouldThrowException() {
        assertThatThrownBy(() -> new User(UUID.randomUUID(), "João", "invalid-email", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void constructor_withNullPassword_shouldThrowException() {
        assertThatThrownBy(() -> new User(UUID.randomUUID(), "João", "joao@example.com", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("User password cannot be null");
    }
}
