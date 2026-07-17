# 1. JWT Symmetric Authentication with Spring Security

Date: 2026-07-17

## Status

Accepted

## Context

We need to restrict access to the Todo REST API so that users can only view, edit, and delete their own todo items. We want a stateless, token-based authentication mechanism that integrates the React frontend and Spring Boot backend.

## Decision

We will implement authentication using JWT (JSON Web Tokens) with a symmetric signature algorithm (HS256).

- **Backend Dependencies:**
  - `spring-boot-starter-oauth2-resource-server` to leverage standard Spring Security resource server configurations.
  - `spring-boot-configuration-processor` to manage JWT configuration properties.
- **Protocol Details:**
  - Symmetric signing (HS256) with a secret key configured via application properties.
  - Authentication endpoint: `POST /api/authenticate` accepting `{ "email": "...", "password": "..." }`.
  - On success, return the JWT token along with the user's UUID (`userId`), name, and email.
- **User Credentials & Storage:**
  - Extend the `User` domain model to hold a BCrypt hashed password in the database.
  - Pre-seed test users (`joao@teste.com` and `maria@teste.com` with password `dummy`) during bootstrap.
  - No public registration or automatic on-the-fly creation will be exposed.

## Consequences

- All endpoints under `/api/users/**` will require a valid `Authorization: Bearer <token>` header.
- The React frontend must capture the token and include it in all outgoing API requests.
- Local tests and Database Seeders must be updated to accommodate BCrypt hashed passwords.
