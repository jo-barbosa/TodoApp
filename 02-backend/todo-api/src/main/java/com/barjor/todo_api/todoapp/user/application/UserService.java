package com.barjor.todo_api.todoapp.user.application;

import com.barjor.todo_api.todoapp.user.domain.User;
import com.barjor.todo_api.todoapp.user.domain.UserRepository;
import com.barjor.todo_api.todoapp.user.domain.exception.EmailAlreadyExistsException;
import com.barjor.todo_api.todoapp.user.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        User user = new User(UUID.randomUUID(), name, email);
        return userRepository.save(user);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
