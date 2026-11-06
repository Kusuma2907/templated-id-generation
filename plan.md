# Implementation Plan

## Phase 1 — Configuration

Load templates.json and cities.csv during application startup.

## Phase 2 — Persistence

Create MySQL entities and repositories for counters and issued IDs.

## Phase 3 — Counter Allocation

Implement database-backed concurrency-safe sequence allocation.

## Phase 4 — Template Rendering

Implement CITY, FY, CY, SEQ and named SEQ placeholders.

## Phase 5 — APIs

Implement:

- single issuance
- bulk issuance
- templates
- cities
- recent IDs
- health

## Phase 6 — Validation and Errors

Implement validation and structured API errors.

## Phase 7 — Frontend

Implement a thin HTML/JavaScript frontend.

## Phase 8 — Testing

Test template rendering, API behavior, validation and concurrency.

## Phase 9 — Containerization

Create Dockerfile and docker-compose.yml.

## Phase 10 — Documentation

Complete README, specification, implementation plan and AI log.

## Future Improvements

With additional development time:

- optimize bulk counter allocation
- add metrics
- add authentication
- add rate limiting
- add OpenAPI documentation
- add distributed deployment support
- add administrative configuration management