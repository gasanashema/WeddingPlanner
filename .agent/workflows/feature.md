---
description: 
---

# NEW FEATURE WORKFLOW

Use this workflow whenever implementing
a new feature.

Follow all applicable project rules.

## STEP 1: Understand the Request

Identify:

- requested functionality
- user roles involved
- business rules
- expected behavior
- affected modules
- dependencies
- acceptance criteria

Do not implement unrelated features.

Ask questions if essential requirements
are unclear.

## STEP 2: Inspect Existing Code

Inspect the relevant:

- backend modules
- frontend pages
- components
- database entities
- migrations
- API endpoints
- tests

Identify existing functionality
that can be reused.

## STEP 3: Check Architecture and Security

Determine:

- where the feature belongs
- what data it needs
- who can access it
- who can modify it
- whether it contains private information
- whether it needs rate limiting
- whether it needs transactions

Follow the existing architecture.

## STEP 4: Plan the Implementation

Present a short implementation plan.

Identify:

- files to create
- files to modify
- database changes
- API changes
- frontend changes
- tests required

Stay within the current phase.

Do not begin future phases.

## STEP 5: Implement Backend

When the feature requires backend changes:

1. Create or update entities if necessary.
2. Create or update repositories.
3. Implement business logic.
4. Implement validation.
5. Implement authorization.
6. Implement endpoints.
7. Handle errors.
8. Add transactions where needed.

Follow backend and security rules.

## STEP 6: Implement Frontend

When the feature requires frontend changes:

1. Inspect the existing design.
2. Reuse existing components.
3. Follow established layout patterns.
4. Integrate with the existing API client.
5. Implement loading and error states.
6. Implement success and empty states.
7. Verify responsive behavior.

Do not redesign existing screens.

## STEP 7: Test

Run relevant tests.

Test:

- successful behavior
- invalid input
- authorization
- business rules
- important error scenarios
- frontend interactions

Add regression tests where appropriate.

## STEP 8: Review

Review the implementation against:

- project standards
- security
- backend architecture
- database rules
- frontend consistency
- testing requirements

Fix violations before completion.

## STEP 9: Report and Stop

Report:

- implemented functionality
- changed files
- database changes
- endpoints added or modified
- tests executed
- test results
- remaining issues

STOP.

Do not start another feature
or the next phase until the user
explicitly confirms.