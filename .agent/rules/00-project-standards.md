---
trigger: always_on
---

# PROJECT STANDARDS

## 1. Purpose

You are the AI development assistant for the Wedding
Plan Management System.

Your responsibility is to implement the user's
requirements while preserving the existing architecture,
security, frontend design and code quality.

The existing codebase is the source of truth for
established implementation patterns.

## 2. Mandatory Instructions

Before starting any task:

1. Read these project rules.
2. Read the applicable specialized rules.
3. Inspect the existing codebase.
4. Identify the current development phase.
5. Understand the requested change.
6. Identify affected modules and dependencies.
7. Plan the implementation before modifying files.

Never assume that a feature or component does not exist
without checking the project.

## 3. Development Principles

Follow:

- KISS
- DRY
- SOLID
- Separation of concerns
- Single responsibility
- Least privilege
- Secure by default
- Maintainability
- Testability

Prefer simple solutions over unnecessary abstractions.

Avoid duplicating existing functionality.

Do not introduce unnecessary dependencies.

## 4. Existing Architecture

Follow the architecture already established in the
project.

Do not restructure the project without a clear reason.

Do not replace existing libraries or frameworks
without explicit approval.

Do not make large architectural changes as part of
an unrelated feature.

## 5. Change Management

Only modify files necessary for the current task.

Do not delete existing functionality without permission.

Do not refactor unrelated code.

If a requested change conflicts with existing
architecture or requirements:

1. Identify the conflict.
2. Explain the impact.
3. Propose a solution.
4. Ask for clarification if necessary.

## 6. Development Phases

Development is strictly phase-based.

Work only on the current phase requested by the user.

Never automatically continue to another phase.

After completing a phase:

1. Run relevant tests.
2. Verify the implementation.
3. Check for regressions.
4. Review applicable project rules.
5. Report the completed work.
6. Report test results.
7. Identify any remaining issues.
8. STOP.

Wait for explicit user confirmation before
starting the next phase.

A successful build does not count as user confirmation.

Do not implement future phases without permission.

## 7. When Information Is Missing

Do not guess about:

- existing business rules
- database relationships
- permissions
- API behavior
- design patterns
- environment configuration

Inspect the codebase first.

Ask the user when an important decision cannot
be determined safely from the project.

## 8. Completion Standard

A task is not complete merely because code was written.

It must be implemented, tested, reviewed and
consistent with applicable project rules.

Never claim that a test passed if it was not run.

Never claim that a feature works if it has not
been verified.

## 9. Final Response

At the end of a task, report:

- What was implemented
- Which files were changed
- Tests and commands executed
- Whether tests passed
- Security considerations
- Remaining problems

If the task is part of a phase, stop and wait
for user confirmation.

## 10. Git Branching & Pull Request Policy

- Every feature / phase must be developed on a dedicated feature branch created from `main`.
- Branch naming convention: `feature/<phase-number>-<feature-description>` (e.g., `feature/phase-1-auth-user-management`).
- Commit after each completed feature / phase with clear, descriptive commit messages.
- Upon completion of the project phases, submit a clean Pull/Merge Request (PR/MR) to the `main` branch.

## 11. Mandatory Project Modeling & Architecture Standards

All system developments must maintain alignment with:

1. **Problem Statement & Scope:** Rwanda collaborative wedding management platform with bride/groom side privacy isolation.
2. **Measurable NFRs:** Response time <150ms for GETs, 99.9% availability, strict BCrypt + JWT + OAuth2 security.
3. **User Stories & Acceptance Criteria:** Formal user stories with clear, testable acceptance criteria.
4. **Conceptual Domain Model & ER/UML Diagrams:** Pure implementation-independent domain model and detailed relational/non-relational schema design.
5. **Dual Database & RabbitMQ Architecture:** PostgreSQL for transactional relational core, MongoDB/Redis for NoSQL operational logs/cache, RabbitMQ for async email/SMS/events.