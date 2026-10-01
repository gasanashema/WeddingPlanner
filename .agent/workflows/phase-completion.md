---
description: 
---

# PHASE COMPLETION WORKFLOW

This workflow is mandatory
at the end of every development phase.

## STEP 1: Confirm Phase Scope

Review the current phase requirements.

Confirm that only work belonging
to this phase was implemented.

Identify unfinished requirements.

Do not silently defer required work.

## STEP 2: Verify Functionality

Run the relevant tests.

Verify the expected user workflows.

Check:

- backend behavior
- frontend behavior
- database behavior
- API integration
- business rules

## STEP 3: Security Review

Check applicable security requirements.

Verify:

- authentication
- authorization
- wedding membership
- privacy scopes
- input validation
- IDOR protection
- rate limiting where applicable
- sensitive data exposure

Run relevant negative tests.

## STEP 4: Design Review

For frontend changes, compare
the new implementation with
the existing frontend.

Check:

- colors
- typography
- spacing
- components
- layout
- responsive behavior
- interaction patterns
- loading and error states

Fix visual inconsistencies.

## STEP 5: Regression Review

Run relevant existing tests.

Check for:

- broken existing features
- unexpected API changes
- database compatibility issues
- permission regressions
- frontend regressions

## STEP 6: Inspect Changes

Review the changed files.

Remove:

- unused code
- temporary debugging statements
- accidental files
- unnecessary dependencies
- unrelated modifications

Confirm that no secrets
or sensitive configuration were added.

## STEP 7: Report

Provide:

### Phase
Name and objective.

### Completed
Implemented requirements.

### Files Changed
Important files and their purpose.

### Verification
Commands, tests and results.

### Security
Security checks and any limitations.

### Remaining Issues
Anything incomplete or unverified.

### Status
Complete and awaiting user confirmation,
or incomplete and requiring further work.

## STEP 8: Mandatory Stop

After reporting:

STOP.

Do not implement the next phase.

Do not create future-phase files.

Do not begin the next module.

Do not assume that passing tests
means the user has approved progression.

Wait for explicit confirmation.

Only continue after the user
confirms that the current phase
works correctly or explicitly
instructs you to proceed.