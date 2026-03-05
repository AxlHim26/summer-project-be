# JobHunter Backend API

[![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.3-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)

Backend service for the JobHunter platform, built with Spring Boot, PostgreSQL, JWT authentication, WebSocket support, and OpenAPI documentation.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [Environment Variables](#environment-variables)
- [Useful URLs](#useful-urls)
- [Developer Commands](#developer-commands)
- [Troubleshooting](#troubleshooting)

## Tech Stack

- Spring Boot 3.5.3 (Web, Validation, Security, Data JPA, Actuator, WebSocket)
- Java 21
- PostgreSQL 17
- JWT (`jjwt`)
- OpenAPI/Swagger UI (`springdoc-openapi`)
- Docker + Docker Compose

## Project Structure

```text
src/main/java/com/jobhunter/jobhunter_be
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
│   └── impl
└── util
```

## Quick Start

### Option A: Docker Dev Mode (recommended)

1. Create local env file:

```bash
cp .env.example .env
```

2. Start app + database (with hot reload via `spring-boot:run`):

```bash
docker compose -f docker-compose.dev.yml up --build
```

### Option B: Docker Production-like Mode

```bash
cp .env.example .env
docker compose up --build
```

### Option C: Run App Locally + DB in Docker

1. Start PostgreSQL (and optional pgAdmin):

```bash
cp .env.example .env
docker compose -f local-docker-compose.yml up -d
```

2. Run backend locally:

```bash
./mvnw spring-boot:run
```

## Environment Variables

Use `.env.example` as template.

| Variable | Description | Default |
| --- | --- | --- |
| `APP_PORT` | Backend port | `8080` |
| `POSTGRES_PORT` | PostgreSQL host port | `5432` |
| `POSTGRES_DB` | Database name | `jobhunter` |
| `POSTGRES_USER` | Database user | `postgres` |
| `POSTGRES_PASSWORD` | Database password | `postgres` |
| `JWT_BASE64_SECRET` | JWT signing secret (Base64) | sample value in `.env.example` |
| `JWT_ACCESS_TOKEN_EXPIRATION` | Access token TTL (ms) | `3600000` |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Refresh token TTL (ms) | `2592000000` |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins | local FE + deployed FE |
| `WEBSOCKET_ALLOWED_ORIGINS` | Allowed WebSocket origins | local FE origins |
| `FRONTEND_BASE_URL` | FE base URL for reset links | `http://localhost:3000` |
| `PGADMIN_PORT` | pgAdmin host port | `5050` |

## Useful URLs

- API base: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Health check: `http://localhost:8080/actuator/health`
- pgAdmin (if enabled): `http://localhost:5050`

## Developer Commands

```bash
# Run tests
./mvnw test

# Build jar
./mvnw clean package

# Stop compose services
docker compose down
docker compose -f docker-compose.dev.yml down
docker compose -f local-docker-compose.yml down
```

## Troubleshooting

- Port already in use:
  - Change `APP_PORT`, `POSTGRES_PORT`, or `PGADMIN_PORT` in `.env`.
- DB connection refused:
  - Verify database container is healthy.
  - Ensure `SPRING_DATASOURCE_URL` points to running Postgres.
- CORS/WebSocket blocked:
  - Update `CORS_ALLOWED_ORIGINS` and `WEBSOCKET_ALLOWED_ORIGINS` in `.env`.
- JWT issues:
  - Make sure `JWT_BASE64_SECRET` is set and consistent across runs.
