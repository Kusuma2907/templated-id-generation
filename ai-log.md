# AI Usage Log

## AI Tools Used

AI coding assistance was used for architecture review,
implementation assistance, testing and code review.

## Prompt 1 — Architecture

Design a production-oriented but time-boxed Spring Boot service for
a templated ID generation system. The system must support single
issuance, bulk issuance, MySQL persistence, concurrency-safe
counters, structured errors and a simple frontend.

## Prompt 2 — Counter Concurrency

Review the proposed counter allocation approach for race conditions
under multiple concurrent HTTP requests. The application may
eventually run on multiple instances, so JVM-only synchronization is
not sufficient.

## Prompt 3 — Template Processing

Implement a small template renderer supporting CITY, FY, CY, SEQ and
named SEQ placeholders according to the assignment grammar.

## Prompt 4 — Testing

Create tests for successful issuance, invalid templates, invalid
cities, missing counter configuration and concurrent requests.

## Prompt 5 — Docker

Review the Docker Compose configuration and ensure a clean checkout
can be started using docker compose up.

## AI Corrections

AI initially suggested approaches based on an in-memory counter /
read-modify-write approach. I identified that this would not be safe
under concurrent requests and would also fail if the service were
scaled to multiple application instances.

I replaced that approach with database-backed counter allocation
using a transaction and pessimistic row locking.

I also added a unique constraint on the formatted identifier as a
second protection against duplicates.

Finally, I verified the behavior with parallel HTTP requests instead
of assuming the implementation was correct from code inspection.