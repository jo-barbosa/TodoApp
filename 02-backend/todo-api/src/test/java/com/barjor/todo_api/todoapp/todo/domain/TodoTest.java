package com.barjor.todo_api.todoapp.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TodoTest {

    @Test
    void constructor_withValidFields_shouldCreateTodo() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate dueDate = LocalDate.now();

        Todo todo = new Todo(id, "Buy Milk", "2 cartons", false, dueDate, userId);

        assertThat(todo.getId()).isEqualTo(id);
        assertThat(todo.getTitle()).isEqualTo("Buy Milk");
        assertThat(todo.getDescription()).isEqualTo("2 cartons");
        assertThat(todo.isCompleted()).isFalse();
        assertThat(todo.getDueDate()).isEqualTo(dueDate);
        assertThat(todo.getUserId()).isEqualTo(userId);
    }

    @Test
    void constructor_withNullId_shouldThrowException() {
        assertThatThrownBy(() -> new Todo(null, "Title", "Desc", false, LocalDate.now(), UUID.randomUUID()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void constructor_withBlankTitle_shouldThrowException() {
        assertThatThrownBy(() -> new Todo(UUID.randomUUID(), "", "Desc", false, LocalDate.now(), UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Todo title cannot be null or blank");
    }

    @Test
    void complete_shouldReturnNewCompletedInstance() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Todo todo = new Todo(id, "Title", "Desc", false, LocalDate.now(), userId);

        Todo completedTodo = todo.complete();

        assertThat(completedTodo.isCompleted()).isTrue();
        assertThat(completedTodo.getId()).isEqualTo(id);
        assertThat(todo.isCompleted()).isFalse(); // original remains unchanged
    }

    @Test
    void update_shouldReturnNewUpdatedInstance() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Todo todo = new Todo(id, "Original Title", "Original Desc", false, LocalDate.now(), userId);

        LocalDate newDueDate = LocalDate.now().plusDays(5);
        Todo updatedTodo = todo.update("New Title", "New Desc", newDueDate, true);

        assertThat(updatedTodo.getTitle()).isEqualTo("New Title");
        assertThat(updatedTodo.getDescription()).isEqualTo("New Desc");
        assertThat(updatedTodo.getDueDate()).isEqualTo(newDueDate);
        assertThat(updatedTodo.isCompleted()).isTrue();
        assertThat(todo.getTitle()).isEqualTo("Original Title"); // original remains unchanged
    }
}
