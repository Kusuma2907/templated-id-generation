# Templated ID Generation Service — Specification

## 1. Problem

Build a service that issues human-readable identifiers using
configurable templates.

The service supports single and bulk identifier generation.

## 2. Configuration

The supplied `templates.json` and `cities.csv` files are treated as
system configuration.

They are loaded at application startup and are not editable through
the application.

## 3. City Input

The API accepts the supplied city_code as the city identifier.

## 4. Financial Year

The Indian financial year is used.

The financial year starts in April and ends in March.

For example:

April 2026 through March 2027 = 2026-27.

## 5. Sequence Scope

Plain [SEQ:n] counters are scoped by:

template + city.

Named counters such as SEQ_WS and SEQ_BPA_PERMIT are scoped by:

named counter + city.

## 6. Named Counter Configuration

Named counters must have configuration in templates.json.

If a template references a named counter that is not configured,
the request fails.

Therefore EST_ALLOTMENT intentionally fails because
SEQ_EST_ALLOT is not present in the counters configuration.

## 7. Bulk Count

Bulk count must be between 1 and 1000.

## 8. Concurrency

MySQL is the source of truth for sequence allocation.

Counter rows are locked using a database transaction and pessimistic
locking.

Issued formatted IDs also have a unique database constraint.

## 9. Error Handling

Errors are returned as structured JSON containing:

- error code
- message
- timestamp

## 10. Frontend

The frontend provides:

- template selection
- city selection
- count
- issue action
- issued ID display
- recently issued ID display

## 11. Out of Scope

- Authentication
- Authorization
- Admin configuration UI
- Runtime template editing
- Rate limiting
- Distributed deployment
- User management
- Advanced frontend styling
- External notification systems