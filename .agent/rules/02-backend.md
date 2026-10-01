---
trigger: always_on
---

# BACKEND DEVELOPMENT STANDARDS

## 1. Existing Implementation

Inspect the existing backend before adding code.

Follow its established:

- package structure
- naming conventions
- dependency injection
- API conventions
- exception handling
- DTO patterns
- persistence approach

Do not introduce a competing architecture.

## 2. Responsibilities

Controllers:

- handle HTTP requests
- validate request structures
- call services
- return appropriate responses

Services:

- implement business logic
- enforce business rules
- coordinate operations
- manage transactions when needed

Repositories:

- handle persistence
- provide database access

DTOs:

- define API contracts
- avoid exposing persistence entities directly

Mappers:

- convert between DTOs and entities
  where appropriate

## 3. Business Logic

Keep business logic in services or
the appropriate domain layer.

Do not place complex business rules
inside controllers.

Avoid duplicating the same business
logic across endpoints.

## 4. API Standards

Use appropriate HTTP methods:

- GET
- POST
- PUT
- PATCH
- DELETE

Use meaningful endpoint names.

Use consistent request and response formats.

Return appropriate HTTP status codes.

Use centralized exception handling.

Never expose stack traces or internal
technical details in API responses.

## 5. Validation

Use the project's established validation
approach.

Validate request DTOs.

Validate business constraints in services.

Do not rely exclusively on frontend validation.

## 6. Authentication and Authorization

Apply security rules to every protected endpoint.

Verify the current user's permissions
before reading or changing resources.

Do not rely on IDs supplied by the client
as proof of ownership.

## 7. Transactions

Use transactions when multiple operations
must succeed or fail together.

Examples:

- wedding creation and member setup
- invitation acceptance
- related financial operations
- multi-record RSVP changes

Avoid unnecessary transactions.

## 8. Error Handling

Use meaningful application exceptions.

Use consistent error codes.

Return safe messages to clients.

Log technical details appropriately.

Do not use generic success responses
for failed operations.

## 9. Performance

Avoid:

- N+1 queries
- unbounded collection queries
- unnecessary entity loading
- repeated database calls
- inefficient filtering

Use pagination for growing collections.

Add indexes when justified by access patterns.

## 10. Configuration

Do not hard-code environment-specific values.

Use the project's configuration system
for secrets and environment settings.

Keep development and production
configuration appropriately separated.

## 11. Documentation

Document important endpoints and
non-obvious business rules.

Keep API documentation aligned
with the implementation.

## 12. Testing

Test:

- service business logic
- controller behavior
- validation
- error handling
- authorization
- persistence
- important integration flows

Include negative tests for protected operations.