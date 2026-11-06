# Templated ID Generation Service

A concurrency-safe service for generating human-readable IDs from configurable templates.

## Technology

- Java 25
- Spring Boot 4.1.2
- Spring MVC
- Spring Data JPA / Hibernate
- MySQL 8.4
- Docker Compose
- Nginx
- Plain HTML/CSS/JavaScript

## Architecture

The application uses:

Controller
→ Service
→ Repository
→ MySQL

Configuration is loaded from the supplied:

- data/templates.json
- data/cities.csv

at application startup.

## Run with Docker

From the repository root:

```bash
docker compose up --build