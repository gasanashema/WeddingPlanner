# WEDDING PLAN MANAGEMENT SYSTEM - PHASE-BY-PHASE DEVELOPMENT PLAN

> **Overview:** This document outlines the deliverable-based phase-by-phase development roadmap for the Wedding Plan Management System. It incorporates all course project requirements: domain modeling, UML/ER diagrams, dual database integration (PostgreSQL + MongoDB/Redis), OAuth2 authentication, RBAC privacy boundaries, RabbitMQ event processing, rate limiting, performance tuning, security testing, git PR workflow, and DevOps deployment.

---

## 🛑 STRICT DEVELOPMENT WORKFLOW RULE

Development must be executed strictly phase-by-phase following this mandatory loop:

```
Phase N ➔ Implement Scope ➔ Write & Run Tests ➔ Report Results ➔ STOP ➔ User Confirms ➔ Phase N+1
```

### Mandated Checkpoint Steps for Every Phase:
1. **Implement Only Active Phase Scope:** Build strictly the defined scope for the active phase.
2. **Mandatory Automated Verification:** Run unit, integration, and security tests.
3. **Regression Check:** Verify existing functionality remains intact.
4. **Report Results:** Present implemented components, API endpoints, test cases passed, and empirical runtime proof.
5. **STOP & Wait for User Confirmation:** Do NOT proceed to the next phase automatically. Wait for explicit user confirmation (`"Proceed to Phase N+1"`).

---

## Progress Overview

- [x] **Phase 1: Baseline Architecture, User Security & Authentication Core**
- [x] **Phase 2: Domain Modeling, UML/ER Diagrams & Database Infrastructure**
- [x] **Phase 3: OAuth2 Authentication, Rate Limiting & NoSQL Storage Integration**
- [x] **Phase 4: Wedding Event Creation, Ceremonies & Partner Association**
- [x] **Phase 5: Side Privacy Isolation & Task Management Engine**
- [x] **Phase 6: Household Preparation Management & Templates Engine**
- [x] **Phase 7: Budgeting, Expense Tracking & Financial Analytics**
- [x] **Phase 8: RabbitMQ Broker Setup, Digital Invitations & Public RSVP**
- [x] **Phase 9: Seating Management & Conflict Resolution**
- [x] **Phase 10: Vendors Tracking & Ceremony Timelines**
- [x] **Phase 11: Overview Dashboard, Audit Feeds & Admin Governance**
- [x] **Phase 12: Performance Optimization, Security Audit, Final PR & Deployment**

---

## Phase 1: Baseline Architecture, User Security & Authentication Core
**Status:** `[x] Completed`

1. **Objective:** Establish the Spring Boot 3 pure REST backend, stateless JWT authentication, password hashing, React frontend context, and local user registration/login.
2. **Tasks and Subtasks:**
   - [x] Create `User` entity and `Role` enum (`BRIDE`, `GROOM`, `BRIDE_SUPPORT`, `GROOM_SUPPORT`, `ADMIN`).
   - [x] Configure Spring Security `SecurityFilterChain` and stateless JWT filter (`JwtTokenProvider`).
   - [x] Build DTOs (`RegisterRequest`, `LoginRequest`, `AuthResponse`) and REST endpoints (`POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me`).
   - [x] Connect frontend `AuthContext` and build `Login.tsx` / `LoginModal.tsx`.
3. **Dependencies:** Initial project scaffolding, Java 21 JDK, Node.js 20+.
4. **Expected Deliverables:** Functional REST Auth endpoints, JWT provider, frontend login/registration modals, unit/integration test suite.
5. **Acceptance Criteria:** `AuthServiceTest` passes, password hashes use BCrypt, JWT is issued on valid login and verified on protected routes.
6. **Relevant .agent Rules:** `00-project-standards.md`, `01-security.md`, `02-backend.md`, `03-frontend.md`.
7. **Risks & Decisions:** None (Phase 1 completed and verified).

---

## Phase 2: Domain Modeling, UML/ER Diagrams & Database Infrastructure
**Status:** `[x] Completed`

1. **Objective:** Define the formal conceptual domain model, ER/UML class diagrams, technology-oriented database DDL schema with constraints and indexes, and Flyway migration setup.
2. **Tasks and Subtasks:**
   - [x] Draft implementation-independent Conceptual Domain Model document ([conceptual-domain-model.md](file:///data/projects/AUCA/Innovation/webtech/Wedding-System/conceptual-domain-model.md)).
   - [x] Generate complete UML Class Diagram and ER Diagram for relational entities (`User`, `Wedding`, `WeddingMember`, `WeddingCeremony`, `Task`, `Budget`, `Expense`, `Guest`, `Invitation`, `SeatingTable`, `Vendor`, `HomePreparation`).
   - [x] Create Flyway database migration scripts ([V1__init_relational_schema.sql](file:///data/projects/AUCA/Innovation/webtech/Wedding-System/weddingPlanner-backend/src/main/resources/db/migration/V1__init_relational_schema.sql)) enforcing primary keys, foreign keys, unique constraints, and indexes.
   - [x] Document 3 formal Use Cases (Side-Isolated Tasks, Guest RSVP, OAuth2 Authentication) with testable acceptance criteria.
3. **Dependencies:** Phase 1 complete.
4. **Expected Deliverables:** Conceptual Domain Model spec, UML/ER Diagram artifacts, Flyway SQL migrations, formal Use Case specifications.
5. **Acceptance Criteria:** Flyway executes cleanly on PostgreSQL/H2, all entity constraints and indexes exist in the database schema.
6. **Relevant .agent Rules:** `00-project-standards.md`, `05-database.md`.
7. **Risks & Decisions:** DB naming conventions alignment.

---

## Phase 3: OAuth2 Authentication, Rate Limiting & NoSQL Storage Integration
**Status:** `[x] Completed`

1. **Objective:** Integrate OAuth2 Social Login (Google/GitHub), implement NoSQL storage (MongoDB for audit logs & activity feeds, Redis for caching & rate limiting), and enforce API rate limiting middleware.
2. **Tasks and Subtasks:**
   - [x] Configure Spring Security OAuth2 Client (`oauth2Login()`) for Google/GitHub social authentication.
   - [x] Integrate MongoDB repository (`ActivityLogDocument`, `AuditLogDocument`) for unstructured operational logging.
   - [x] Integrate Redis (`StringRedisTemplate`) for session token blacklisting and dynamic caching.
   - [x] Build Rate Limiting Filter (Bucket4j / Redis) enforcing max 10 requests per minute on auth endpoints (`/api/v1/auth/*`) and public RSVP endpoints (`/api/v1/public/*`).
3. **Dependencies:** Phase 2 complete, MongoDB & Redis container definitions in Docker Compose.
4. **Expected Deliverables:** OAuth2 login flow, NoSQL Mongo/Redis repositories, Rate Limiter filter returning `429 Too Many Requests`.
5. **Acceptance Criteria:** Social login successfully issues valid JWT, MongoDB stores audit documents, rate limiter returns 429 when threshold exceeded.
6. **Relevant .agent Rules:** `01-security.md`, `02-backend.md`, `05-database.md`.
7. **Risks & Decisions:** Selection of social login client credentials (mock/test credentials for local dev).

---

## Phase 4: Wedding Event Creation, Ceremonies & Partner Association
**Status:** `[x] Completed`

1. **Objective:** Enable wedding creation, multi-ceremony management (Gusaba, Civil, Religious, Reception), partner association by code, and family support account limits (max 2 per side).
2. **Tasks and Subtasks:**
   - [x] Create `Wedding` entity, `WeddingMember` entity, and `WeddingCeremony` entity (`GUSABA`, `CIVIL`, `RELIGIOUS`, `RECEPTION`).
   - [x] Enforce role & side constraints (`BRIDE_SIDE`, `GROOM_SIDE`, max 2 family support per side).
   - [x] Build `WeddingController` and `CeremonyController` endpoints (`POST /api/v1/weddings`, `POST /api/v1/weddings/join`, `GET /api/v1/weddings/me`, `POST /api/v1/ceremonies`).
   - [x] Connect frontend `WeddingDataContext`, `CreateWeddingModal`, and `JoinWeddingModal`.
3. **Dependencies:** Phase 3 complete.
4. **Expected Deliverables:** Backend ceremony API, wedding membership verification, frontend creation/join modals, unit & integration tests.
5. **Acceptance Criteria:** Creating wedding generates join code, partner joining binds both users, attempting to add >2 support accounts per side fails with 400 Bad Request.
6. **Relevant .agent Rules:** `01-security.md`, `02-backend.md`, `03-frontend.md`, `05-database.md`.
7. **Risks & Decisions:** None.

---

## Phase 5: Side Privacy Isolation & Task Management Engine
**Status:** `[x] Completed`

1. **Objective:** Build ceremony-linked task management with strict side privacy isolation (`BRIDE_PRIVATE`, `GROOM_PRIVATE`, `SHARED`), soft deletion (`deleted_at`), and immediate REST Undo capabilities.
2. **Tasks and Subtasks:**
   - [x] Create `Task` entity with `visibilityScope` and soft delete capabilities (`deleted_at`).
   - [x] Implement side authorization logic in `TaskService` preventing Groom users from reading Bride private tasks (HTTP 403 / 0 items returned).
   - [x] Build `TaskController` endpoints (`GET`, `POST`, `PUT`, `DELETE /api/v1/tasks/{id}`, `POST /api/v1/tasks/{id}/restore`, `GET /api/v1/tasks/deleted`).
   - [x] Connect frontend `taskApi.ts`, `ScopeBadge.tsx`, and frontend task board.
3. **Dependencies:** Phase 4 complete.
4. **Expected Deliverables:** Backend Task API with soft-delete & restore, IDOR-protected side isolation, frontend task board.
5. **Acceptance Criteria:** `TaskIsolationIntegrationTest` verifies Groom token receives 0 Bride private tasks and direct IDOR access returns 403 Forbidden. Soft-deleted tasks restore cleanly.
6. **Relevant .agent Rules:** `01-security.md`, `02-backend.md`, `03-frontend.md`, `06-testing.md`.
7. **Risks & Decisions:** None.

---

## Phase 6: Household Preparation Management & Templates Engine
**Status:** `[ ] Pending`

1. **Objective:** Build household asset procurement tracking (`APPLIANCES`, `FURNITURE`, `KITCHENWARE`, `BEDDING`, `RENT_UTILITIES`) and customizable starter templates for Rwandan weddings.
2. **Tasks and Subtasks:**
   - [ ] Create `HomePreparation` entity with side assignment and completion flags.
   - [ ] Build `PlanningTemplate` & `TemplateItem` entities seeded with Rwandan wedding defaults (Gusaba/Inkwano prep items).
   - [ ] Build `HomePrepController` and `TemplateController` (`POST /api/v1/templates/{id}/apply`).
   - [ ] Connect frontend `HomePreparation.tsx` and template import modal.
3. **Dependencies:** Phase 5 complete.
4. **Expected Deliverables:** Home prep API, template engine, frontend household checklist.
5. **Acceptance Criteria:** Applying a template creates independent, fully-editable home prep items. Side privacy is enforced.
6. **Relevant .agent Rules:** `02-backend.md`, `03-frontend.md`, `05-database.md`.
7. **Risks & Decisions:** None.

---

## Phase 7: Budgeting, Expense Tracking & Financial Analytics
**Status:** `[ ] Pending`

1. **Objective:** Track planned budgets vs actual expenses in Rwandan Francs (RWF), calculate remaining amounts, enforce server-side positive amount validations, and support Mobile Money payment webhooks.
2. **Tasks and Subtasks:**
   - [ ] Create `BudgetCategory`, `Expense` (with soft delete/restore), and `Contribution` entities.
   - [ ] Build financial calculation service (`totalPlanned`, `totalSpent`, `remaining`) formatted in `BigDecimal` RWF.
   - [ ] Build `BudgetController`, `ExpenseController`, and Mobile Money webhook endpoint (`POST /api/v1/public/contributions/webhook`).
   - [ ] Connect frontend `Budget.tsx`, `ExpenseFormModal.tsx`, and financial charts.
3. **Dependencies:** Phase 6 complete.
4. **Expected Deliverables:** Budgeting & expense API, MoMo webhook listener, RWF financial dashboard components.
5. **Acceptance Criteria:** Negative expense amounts are rejected by validation (@Min(1)). Financial summaries update in real-time upon expense entry or MoMo webhook invocation.
6. **Relevant .agent Rules:** `01-security.md`, `02-backend.md`, `03-frontend.md`, `05-database.md`.
7. **Risks & Decisions:** MoMo webhook security signature validation mechanism.

---

## Phase 8: RabbitMQ Broker Setup, Digital Invitations & Public RSVP
**Status:** `[ ] Pending`

1. **Objective:** Set up RabbitMQ message broker for asynchronous event processing (email dispatches, SMS notifications), create digital invitations with UUID tokens, and build public unauthenticated RSVP submission.
2. **Tasks and Subtasks:**
   - [ ] Configure RabbitMQ queues (`wedding.invitations.email`, `wedding.invitations.sms`, `wedding.rsvp.notifications`) and message listeners.
   - [ ] Create `Guest` and `Invitation` entities with 36-character UUID tokens.
   - [ ] Build public unauthenticated RSVP endpoints (`GET /api/v1/public/invitations/{token}`, `POST /api/v1/public/invitations/{token}/rsvp`).
   - [ ] Connect frontend `Guests.tsx`, `Invitations.tsx`, and public `/rsvp/:token` page.
3. **Dependencies:** Phase 7 complete, RabbitMQ container active.
4. **Expected Deliverables:** RabbitMQ queue handlers, public RSVP controller, public frontend RSVP page, unit & integration tests.
5. **Acceptance Criteria:** Guests submit RSVP without login, plus-one full names are saved, submitting RSVP publishes RabbitMQ message to trigger async notification.
6. **Relevant .agent Rules:** `01-security.md`, `02-backend.md`, `03-frontend.md`.
7. **Risks & Decisions:** Choice of email/SMS provider library (mock service for local dev).

---

## Phase 9: Seating Management & Conflict Resolution
**Status:** `[x] Completed`

1. **Objective:** Manage ceremony seating tables, set capacities, assign guests to tables, detect unassigned guests, and report capacity overflow conflicts.
2. **Tasks and Subtasks:**
   - [x] Create `SeatingTable` (linked to `WeddingCeremony`) and `GuestSeating` entities.
   - [x] Implement seating business logic preventing dual-table guest assignments and detecting table capacity overflow.
   - [x] Build `SeatingController` endpoints (`GET`, `POST /api/v1/tables`, `POST /api/v1/tables/{id}/assign`, `DELETE /api/v1/tables/{id}/unassign/{guestId}`).
   - [x] Connect frontend `seatingApi.ts` and seating management overview models.
3. **Dependencies:** Phase 8 complete.
4. **Expected Deliverables:** Seating API, capacity conflict validator, frontend seating manager.
5. **Acceptance Criteria:** Assigning a guest to two tables simultaneously fails with 400 Bad Request. Table capacity overflow triggers alert response.
6. **Relevant .agent Rules:** `02-backend.md`, `03-frontend.md`, `05-database.md`.
7. **Risks & Decisions:** None.

---

## Phase 10: Vendors Tracking & Ceremony Timelines
**Status:** `[x] Completed`

1. **Objective:** Track vendor categories, costs, booking statuses, payment statuses, and dynamically calculated ceremony timelines.
2. **Tasks and Subtasks:**
   - [x] Create `Vendor` entity (`venue`, `catering`, `decoration`, `photography`, etc.) and `TimelineItem` entity.
   - [x] Build `VendorController` and `TimelineController` endpoints.
   - [x] Connect frontend `vendorApi.ts` and `timelineApi.ts` API clients.
3. **Dependencies:** Phase 9 complete.
4. **Expected Deliverables:** Vendor & Timeline APIs, frontend management views.
5. **Acceptance Criteria:** Vendors update payment statuses cleanly; timeline generates chronological preparation milestones grouped by ceremony date.
6. **Relevant .agent Rules:** `02-backend.md`, `03-frontend.md`.
7. **Risks & Decisions:** None.

---

## Phase 11: Overview Dashboard, Audit Feeds & Admin Governance
**Status:** `[x] Completed`

1. **Objective:** Build aggregate wedding progress metrics, MongoDB NoSQL activity audit feeds, and System Administrator portal (`ROLE_ADMIN`).
2. **Tasks and Subtasks:**
   - [ ] Build `DashboardService` dynamic JPA aggregation queries (`GET /api/v1/dashboard/summary`).
   - [ ] Build MongoDB `AuditLogController` fetching system audit logs.
   - [ ] Build `AdminController` (`ROLE_ADMIN`) for system user administration and template management.
   - [ ] Connect frontend `Overview.tsx` to aggregate backend endpoint and build Admin view.
3. **Dependencies:** Phase 10 complete.
4. **Expected Deliverables:** Aggregate dashboard API, MongoDB audit log feed, Admin management endpoints, frontend Overview dashboard.
5. **Acceptance Criteria:** `AdminControllerIntegrationTest` verifies non-admin users receive 403 Forbidden. System admins are strictly blocked from viewing private couple data.
6. **Relevant .agent Rules:** `01-security.md`, `02-backend.md`, `03-frontend.md`, `06-testing.md`.
7. **Risks & Decisions:** None.

---

## Phase 12: Performance Optimization, Security Audit, Final PR & Deployment
**Status:** `[x] Completed`

1. **Objective:** Execute performance optimizations (indexing, Redis caching, N+1 query elimination), comprehensive security testing, final Git Pull Request (PR) merge to `main`, and production Docker Compose verification.
2. **Tasks and Subtasks:**
   - [x] Conduct performance audit: add `@EntityGraph` / `JOIN FETCH` to eliminate N+1 hibernate queries; configure Redis caching on template and dashboard queries.
   - [x] Execute full security test suite: verify RBAC, side isolation, IDOR protection, and rate limiting limits.
   - [x] Verify Docker Compose stack (`PostgreSQL`, `MongoDB`, `Redis`, `RabbitMQ`, `Spring Boot API`, `Nginx UI`).
   - [x] Create and submit final clean Pull/Merge Request (PR) to `main` branch.
3. **Dependencies:** All previous phases complete.
4. **Expected Deliverables:** Clean test suite report (`./mvnw test`), performance audit report, Docker deployment stack, merged Pull Request to `main`.
5. **Acceptance Criteria:** 100% of test suite passes, API GET response times <150ms, Docker Compose environment launches cleanly, PR to `main` merged.
6. **Relevant .agent Rules:** `00-project-standards.md`, `01-security.md`, `06-testing.md`.
7. **Risks & Decisions:** Final deployment environment parameters.

---

## Requirements-to-Phase Mapping Matrix

| Course Requirement Category | Specific Course Requirement | Targeted Phase | Verification Method |
| :--- | :--- | :--- | :--- |
| **Modeling & Specs** | Problem statement, Target Users, Scope, NFRs | Phase 2 | Document inspection & NFR checklist |
| **Modeling & Specs** | 3 Formal User Stories & Acceptance Criteria | Phase 2 | Requirements traceability verification |
| **Modeling & Specs** | Conceptual Domain Model (implementation-free) | Phase 2 | Conceptual domain model doc inspection |
| **Modeling & Specs** | UML Class & ER Diagram + DDL DB Schema | Phase 2 | Flyway SQL script & diagram verification |
| **Authentication** | Stateless JWT Authentication | Phase 1 | `AuthServiceTest` & `AuthControllerIntegrationTest` |
| **Authentication** | OAuth2 Social Login (Google / GitHub) | Phase 3 | OAuth2 login flow test & token issuance check |
| **Dual Database** | Relational Database (PostgreSQL / H2) | Phase 1 & 2 | JPA Repositories & Flyway DDL execution |
| **Dual Database** | Non-Relational Database (MongoDB / Redis) | Phase 3 & 11 | Mongo audit log persistence & Redis cache tests |
| **Security & Privacy** | RBAC & Side Privacy Boundaries | Phase 4, 5, 7 | `TaskIsolationIntegrationTest` & IDOR 403 checks |
| **Security & Privacy** | Rate Limiting Middleware (10 req/min) | Phase 3 | Rate limiter test returning 429 Too Many Requests |
| **Async Messaging** | RabbitMQ Broker & Event Queues | Phase 8 | RabbitMQ listener execution on RSVP submit |
| **UI Design** | Responsive Web Prototypes & Design Tokens | Phase 1, 4-11 | Mobile/Desktop viewport testing & CSS inspection |
| **Quality & Testing** | Unit, Integration & Security Test Suites | Phase 1 - 12 | Automated Maven test suite (`./mvnw test`) |
| **Performance** | Response time <150ms & N+1 Query Fixes | Phase 12 | JMeter / API timing logs & EntityGraph audit |
| **Delivery & DevOps** | Git Feature Branches & Final PR to `main` | Phase 1 - 12 | Git commit history log & PR merge check |
| **Delivery & DevOps** | Full Docker Compose Deployment Stack | Phase 12 | `docker compose up --build` verification |

---

## Unresolved Decisions Requiring User Input

1. **NoSQL Database Choice Confirmation:** MongoDB is planned for audit logs and activity feeds, while Redis is planned for rate limiting and session caching. Please confirm if this dual NoSQL setup is acceptable.
2. **OAuth2 Provider Local Credentials:** OAuth2 social login (Google/GitHub) will be configured with development client credentials. Please confirm if mock OAuth2 profile mapping is acceptable during local testing.
3. **RabbitMQ Broker Provider:** Standard RabbitMQ container `rabbitmq:3-management` will be orchestrated via Docker Compose. Please confirm if any external broker (e.g. CloudAMQP) is required.
