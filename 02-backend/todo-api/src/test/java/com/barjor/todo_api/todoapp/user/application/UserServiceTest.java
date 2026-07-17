package com.barjor.todo_api.todoapp.user.application;

import com.barjor.todo_api.todoapp.user.domain.User;
import com.barjor.todo_api.todoapp.user.domain.UserRepository;
import com.barjor.todo_api.todoapp.user.domain.exception.EmailAlreadyExistsException;
import com.barjor.todo_api.todoapp.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void createUser_whenEmailDoesNotExist_shouldSaveUser() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.createUser("John Doe", "john@example.com", "password");

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("John Doe");
        assertThat(created.getEmail()).isEqualTo("john@example.com");
        assertThat(created.getPassword()).isEqualTo("hashed-password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_whenEmailExists_shouldThrowEmailAlreadyExistsException() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser("John Doe", "john@example.com", "password"))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User mockUser = new User(userId, "John Doe", "john@example.com", "hashed-password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(userId);

        assertThat(result).isEqualTo(mockUser);
    }

    @Test
    void getUserById_whenUserDoesNotExist_shouldThrowUserNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(UserNotFoundException.class);
    }
}
