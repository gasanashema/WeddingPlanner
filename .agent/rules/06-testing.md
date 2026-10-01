---
trigger: always_on
---

# TESTING AND QUALITY ASSURANCE

## 1. General

Testing is mandatory.

Do not claim that functionality works
without appropriate verification.

Use the project's existing
testing frameworks and conventions.

## 2. Before Testing

Inspect:

- existing tests
- test configuration
- build commands
- test database configuration
- relevant test utilities

Reuse existing test infrastructure.

Do not create an unnecessary
parallel testing setup.

## 3. Backend Tests

Use appropriate tests for:

- business logic
- validation
- controllers
- repositories
- authorization
- transactions
- error handling

Test both successful and failed operations.

## 4. Frontend Tests

Where applicable, test:

- rendering
- interactions
- forms
- validation
- loading states
- empty states
- errors
- API integration
- navigation

Follow the existing frontend
testing conventions.

## 5. Security Tests

For protected resources, test:

- authorized access
- unauthorized access
- opposite-side access
- unrelated-user access
- invalid resource IDs
- forbidden updates
- forbidden deletions

Verify that private data is not returned
to unauthorized users.

## 6. Business Rule Tests

Test important project rules.

Examples:

- correct wedding membership
- permitted side access
- shared information visibility
- task assignment restrictions
- valid financial calculations
- RSVP constraints
- seating capacity rules

## 7. Regression Testing

Run relevant existing tests
after changing established functionality.

Do not assume that unrelated
functionality remains unaffected.

Investigate failures before completion.

## 8. Build and Static Checks

Run the applicable:

- build
- unit tests
- integration tests
- linting
- type checking
- formatting checks

Do not skip a check merely because
the feature appears simple.

If a check cannot run,
explain why and identify what remains unverified.

## 9. Manual Verification

Where appropriate, verify
the complete user workflow.

For frontend changes, inspect
the actual page and its states.

For APIs, verify relevant requests
and responses.

Do not substitute a successful
compilation for functional verification.

## 10. Test Results

Report:

- command executed
- result
- failures
- skipped tests
- checks not performed

Never fabricate test results.

## 11. Completion Gate

A feature or phase must not be
declared verified until relevant
checks have been completed.

If important tests fail:

- stay in the current phase
- investigate
- fix
- retest
- report the outcome

Do not continue to another phase
without user confirmation.