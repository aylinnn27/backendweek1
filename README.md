# Library Management REST API

A layered CRUD REST API built with **Spring Boot 3**, **Spring Data JPA**, and a relational database, modeling a simple Library domain: **Author**, **Book**, **Member**.

## Architecture

```
Controller  -->  Service (interface + impl)  -->  Repository  -->  Database
   |                    |
  DTO  <--------  Entity <-> DTO mapping happens inside the Service layer
```

- **Controller**: HTTP layer only — receives requests, validates input (`@Valid`), returns proper status codes.
- **Service**: business logic, entity <-> DTO mapping, exceptions.
- **Repository**: `JpaRepository` interfaces — no logic, just data access.
- **DTOs**: entities are never returned directly from any endpoint. This avoids the classic bidirectional `@OneToMany`/`@ManyToOne` infinite-recursion problem when serializing JPA entities to JSON.
- **Exception handling**: centralized in `GlobalExceptionHandler` via `@RestControllerAdvice`.

## Tech Stack

- Java 17
- Spring Boot 3.3
- Spring Data JPA (Hibernate)
- H2 (default, in-memory, zero config) / MySQL / PostgreSQL (swap-in ready)
- springdoc-openapi (Swagger UI)
- JUnit 5 + Mockito + AssertJ

## Project Structure

```
src/main/java/com/library/
├── entity/          Author, Book, Member (JPA entities)
├── dto/             Request/Response DTOs per entity
├── repository/      Spring Data JPA repositories
├── service/          service interfaces
│   └── impl/        service implementations
├── controller/      REST controllers
└── exception/       ResourceNotFoundException, ErrorResponse, GlobalExceptionHandler
```

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run locally (H2, no setup needed)

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`. H2 console (optional, for inspecting data): `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:librarydb`, user `sa`, empty password).

### Run with MySQL or PostgreSQL

1. Create a database, e.g. `librarydb`.
2. In `src/main/resources/application.yml`, comment out the H2 `datasource` block and uncomment the MySQL or PostgreSQL block, filling in your credentials.
3. `mvn spring-boot:run`

### Environment variables (example `.env` style, if you externalize config)

```
DB_URL=jdbc:mysql://localhost:3306/librarydb
DB_USERNAME=root
DB_PASSWORD=your_password
SERVER_PORT=8080
```

## API Documentation

Once running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

Raw OpenAPI spec: `http://localhost:8080/v3/api-docs`

## Endpoints

| Method | Path                | Description                          |
|--------|---------------------|---------------------------------------|
| POST   | /api/authors        | Create author (201)                  |
| GET    | /api/authors/{id}   | Get author by id (200 / 404)         |
| GET    | /api/authors        | List authors, paginated + sortable   |
| PUT    | /api/authors/{id}   | Update author (200 / 404)            |
| DELETE | /api/authors/{id}   | Delete author (204 / 404)            |
| POST   | /api/books          | Create book (201)                    |
| GET    | /api/books/{id}     | Get book by id (200 / 404)           |
| GET    | /api/books          | List books, paginated + sortable     |
| PUT    | /api/books/{id}     | Update book (200 / 404)              |
| DELETE | /api/books/{id}     | Delete book (204 / 404)              |
| POST   | /api/members        | Register member (201)                |
| GET    | /api/members/{id}   | Get member by id (200 / 404)         |
| GET    | /api/members        | List members, paginated + sortable   |
| PUT    | /api/members/{id}   | Update member (200 / 404)            |
| DELETE | /api/members/{id}   | Delete member (204 / 404)            |

Pagination/sorting example: `GET /api/books?page=0&size=5&sort=title,asc`

### Sample requests

Create an author:
```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name": "George Orwell", "biography": "British author"}'
```

Create a book (requires an existing authorId):
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title": "1984", "isbn": "9780451524935", "publishedDate": "1949-06-08", "authorId": 1}'
```

## Testing

Run unit tests for the service layer:

```bash
mvn test
```

`BookServiceImplTest` covers create (success + author-not-found), get-by-id (success + not-found), and delete, using Mockito to isolate the service from the database.

## Postman

Import `postman_collection.json` (in the repo root) into Postman, or use the Swagger UI link above.
