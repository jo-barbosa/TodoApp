package com.barjor.todo_api.todoapp.common.config;

import com.barjor.todo_api.todoapp.todo.application.TodoService;
import com.barjor.todo_api.todoapp.user.application.UserService;
import com.barjor.todo_api.todoapp.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("bootstrap")
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserService userService;
    private final TodoService todoService;

    public DatabaseSeeder(UserService userService, TodoService todoService) {
        this.userService = userService;
        this.todoService = todoService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("--------------------------------------------------");
        log.info("BOOTSTRAP PROFILE DETECTED: Seeding mock database...");

        User user1 = userService.createUser("João Barbosa", "joao@example.com");
        User user2 = userService.createUser("Maria Silva", "maria@example.com");

        log.info("Mock Users Seeded successfully:");
        log.info("User 1: {} | ID: {} | Email: {}", user1.getName(), user1.getId(), user1.getEmail());
        log.info("User 2: {} | ID: {} | Email: {}", user2.getName(), user2.getId(), user2.getEmail());

        todoService.createTodo(user1.getId(), "Comprar leite", "Comprar meio gordo, 2 pacotes", LocalDate.now());
        todoService.createTodo(user1.getId(), "Estudar Spring Boot e React", "Aprofundar conhecimentos em arquitetura hexagonal", LocalDate.now().plusDays(1));

        todoService.createTodo(user2.getId(), "Marcar consulta médica", "Consulta de rotina no dentista", LocalDate.now().plusWeeks(1));
        todoService.createTodo(user2.getId(), "Limpar o escritório", "Organizar a secretária e os livros", LocalDate.now());

        log.info("Mock Todos Seeded successfully!");
        log.info("--------------------------------------------------");
    }
}
