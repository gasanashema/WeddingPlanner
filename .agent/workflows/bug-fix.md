---
description: 
---

# BUG FIX WORKFLOW

Use this workflow whenever fixing
a defect in the project.

Follow all applicable project rules.

## STEP 1: Understand the Bug

Identify:

- expected behavior
- actual behavior
- error message
- affected feature
- steps to reproduce
- affected users or roles

Do not assume the reported cause
is the actual root cause.

## STEP 2: Reproduce

Inspect the relevant implementation.

Where possible:

- reproduce the issue
- run the failing test
- inspect logs
- inspect API requests
- inspect relevant database records
- inspect browser errors

Do not modify unrelated functionality.

## STEP 3: Find the Root Cause

Identify the actual reason
the behavior is incorrect.

Check relevant:

- business logic
- validation
- authorization
- API contracts
- database relationships
- frontend state
- asynchronous behavior
- configuration

Avoid applying superficial fixes
that leave the root cause unresolved.

## STEP 4: Plan the Fix

Explain briefly:

- root cause
- proposed correction
- affected files
- tests required

If the fix requires a significant
architectural change, ask first.

## STEP 5: Implement

Make the smallest correct change.

Preserve existing functionality.

Do not introduce unnecessary
refactoring or dependencies.

Follow applicable security
and design rules.

## STEP 6: Verify

Run:

- the previously failing test
- relevant tests
- regression tests
- build or lint checks where applicable

Verify the original reproduction steps.

Check that the fix does not
introduce a new security problem.

## STEP 7: Report and Stop

Report:

- root cause
- files changed
- fix implemented
- tests executed
- results
- any remaining uncertainty

STOP.

Do not begin unrelated work
or the next development phase
without explicit confirmation.