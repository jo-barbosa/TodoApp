package com.barjor.todo_api.todoapp.user.application;

import com.barjor.todo_api.todoapp.user.domain.User;
import com.barjor.todo_api.todoapp.user.domain.UserRepository;
import com.barjor.todo_api.todoapp.user.domain.exception.EmailAlreadyExistsException;
import com.barjor.todo_api.todoapp.user.domain.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String name, String email, String plainPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        String hashedPassword = passwordEncoder.encode(plainPassword);
        User user = new User(UUID.randomUUID(), name, email, hashedPassword);
        return userRepository.save(user);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
