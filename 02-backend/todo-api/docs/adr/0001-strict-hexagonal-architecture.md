# Strict Hexagonal Architecture

We decided to implement a strict Hexagonal (Clean) Architecture pattern where domain entities (`User` and `Todo`) are pure Java classes completely independent of the JPA/Hibernate framework. We will use separate database entities (`UserEntity` and `TodoEntity`) in the infrastructure layer and map between them, trading off initial development boilerplate for a clean, decouple-able business domain.
