# Auth Backend - Spring Boot

## Overview

This project is a backend authentication service built with Spring Boot.

It supports login with email and password, as well as a simulated SSO (Single Sign-On) flow.

The service generates JWT tokens and handles authentication, validation, and error management.

---

## Features

* Login with email and password
* JWT token generation
* SSO flow simulation
* Centralized error handling
* Input validation
* Unit and integration testing

---

## Architecture

The project is structured using a modular approach:

* `business/`: core business logic
* `infrastructure/`: controllers, configuration, persistence, and security

### Responsibilities

* **business**
    * Services
    * Domain models
    * Business exceptions

* **infrastructure**
    * REST controllers
    * Security configuration (JWT)
    * Database access (JPA)
    * Exception handling

This separation ensures maintainability, scalability, and testability.

---

## Authentication Flow

### Login

1. User sends email and password
2. Backend validates credentials
3. JWT token is generated
4. Token is returned to the client

---

### SSO Flow (Simulated)

1. Client requests SSO URL
2. Backend returns authorization URL
3. Client simulates redirect
4. Backend receives `code` in callback

    * `valid-code` → success
    * invalid or missing → error

5. JWT token is generated and returned

---

## API Endpoints

### Login

POST `/api/auth/login`

---

### SSO Start

GET `/api/auth/sso`

Returns authorization URL.

---

### SSO Callback

GET `/api/auth/sso/callback?code=...`

Returns JWT if valid.

---

## Configuration

Main configuration is defined in `application.yml`.

Includes:

* Database (H2 in-memory)
* JWT configuration
* SSO properties

---

## Database

The project uses an in-memory H2 database.

A basic `users` table is initialized automatically.

Test user:

* email: `david@test.com`
* password: `123456`

---

## Tech Stack

* Java 11+
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* H2 Database

---

## Testing

* Unit tests with Mockito
* Integration tests with Spring Boot and H2

Run tests:

```bash
mvn test
```

## Run the project:

* Build project:
```bash
mvn clean install
```

* Run application (infrastructure module):
```bash
mvn spring-boot:run -pl infrastructure
```
* Access API:
```bash
http://localhost:8080
```

## Notes
* SSO flow is simulated (no external provider)
* JWT implementation is basic (no refresh token)
* Clear separation between business and infrastructure layers

