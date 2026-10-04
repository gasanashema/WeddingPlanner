# WEDDING PLAN MANAGEMENT SYSTEM DEVELOPMENT SPECIFICATION
**Rwanda-focused collaborative wedding planning platform**

---

## 1. Product Overview & Problem Statement

### Problem Statement
In Rwanda, wedding planning is highly collaborative yet culturally nuanced, involving distinct ceremonies (Gusaba/Gukora Irembo, Civil Marriage, Religious Ceremony, Reception) and separate family responsibilities. Couples currently rely on fragmented tools (paper checklists, phone calls, scattered spreadsheets), causing miscommunication, budget overruns, and privacy breaches between side-specific expenses and home preparations.

### Target Users & Objectives
1. **Bride & Groom:** Primary wedding owners who require a unified dashboard, shared event planning, and private side-specific workspaces.
2. **Bride & Groom Supporting Family Members:** Trusted family members (max 2 per side) assisting with side-specific tasks and expenses.
3. **Guests:** Friends and extended family receiving digital invitations, viewing event itineraries, and submitting RSVPs.
4. **System Administrators:** System supervisors managing templates, system health, and global configurations without accessing private couple data.

### Measurable Quality Attributes (NFRs)
- **Usability:** Responsive web UI adhering to established burgundy/champagne visual design; page load < 2 seconds.
- **Performance:** 95% of core API GET requests complete under **150ms**; dynamic dashboard aggregations complete under **200ms**.
- **Reliability:** 99.9% application availability; database transactions enforced with ACID compliance and optimistic locking (`@Version`); soft deletion (`deleted_at`) with undo endpoints.
- **Security & Privacy:** Server-side authorization verifying `weddingId` and `side`; BCrypt password hashing; stateless JWT + OAuth2 social login; rate-limiting (10 req/min on auth & public endpoints); strict IDOR prevention.

---

## 1b. User Stories & Acceptance Criteria

### User Story 1: Side-Isolated Task & Expense Management (Bride/Groom)
* **As a** Bride or Groom,
* **I want to** create private tasks and home preparation expenses for my side,
* **So that** my partner or their side cannot view private financial or prep details until explicitly shared.
* **Acceptance Criteria:**
  1. Given an authenticated Bride user, when creating a task with scope `BRIDE_PRIVATE`, it is persisted and visible only to the Bride and Bride Support accounts.
  2. Given an authenticated Groom user querying `GET /api/v1/tasks`, the response must filter out all `BRIDE_PRIVATE` items.
  3. Attempting to directly fetch a `BRIDE_PRIVATE` resource ID using a Groom JWT returns `403 Forbidden` (IDOR protection).

### User Story 2: Digital Invitation & Public RSVP (Guest)
* **As a** Wedding Guest,
* **I want to** open a unique invitation link on my mobile browser without logging in, verify my identity, specify plus-one names, and submit my RSVP,
* **So that** the couple receives real-time attendance counts and seating details.
* **Acceptance Criteria:**
  1. Given a valid 36-character UUID invitation token, `GET /api/v1/public/invitations/{token}` returns invitation details without requiring authentication headers.
  2. Submitting `POST /api/v1/public/invitations/{token}/rsvp` validates guest input, captures plus-one full names, and triggers a RabbitMQ async event to notify the couple.
  3. Repeated RSVP requests from the same IP exceeding 10 req/min return `429 Too Many Requests`.

### User Story 3: OAuth2 Authentication & Account Binding
* **As a** New User,
* **I want to** register or log in using my Google or GitHub OAuth2 account,
* **So that** I can securely access the platform without manually managing another password.
* **Acceptance Criteria:**
  1. Clicking "Log in with Google" authenticates via OAuth2 protocol, retrieves verified user claims, and issues a stateless JWT.
  2. If the user email does not exist, an account is automatically created with standard default role assignment.

### User Story 4: Role-Based Account Creation & Partner Invitation Flow
* **As a** Bride or Groom registering a new account,
* **I want to** select my role (Bride or Groom) and enter my partner's name, phone, and email on the onboarding setup page,
* **So that** a shared wedding workspace is automatically created, my partner receives an automated invitation email via RabbitMQ with temporary login credentials, and my partner can log in, set their password, and manage family support accounts.
* **Acceptance Criteria:**
  1. Given a user registering as `ROLE_BRIDE` or `ROLE_GROOM`, providing partner details creates both the user account and partner account (`mustChangePassword` = true).
  2. The system publishes an automated email event to RabbitMQ containing the partner's temporary credentials (`Partner123!`) and password update link.
  3. Logging in as the partner prompts them to set a new password, and grants them couple-level rights to invite family support members (`BRIDE_SUPPORT` / `GROOM_SUPPORT`).

---

## 1c. Architecture & Technology Alignment

### Dual Database Strategy
- **Relational Database (PostgreSQL / H2):** Manages primary transactional entities (`User`, `Wedding`, `WeddingMember`, `WeddingCeremony`, `Task`, `Budget`, `Expense`, `Guest`, `Invitation`, `SeatingTable`, `Vendor`, `HomePreparation`). Enforces strict primary keys, foreign keys, constraints, and Flyway schema migrations.
- **Non-Relational Database (MongoDB / Redis):**
  - **MongoDB:** High-volume system audit logs, real-time activity feeds, and invitation link view tracking documents.
  - **Redis:** Token blacklist, session cache, dynamic performance caching, and distributed rate-limiting buckets.

### Asynchronous Event Processing (RabbitMQ)
- **Queues:** `wedding.invitations.email` (email delivery), `wedding.invitations.sms` (SMS dispatch), `wedding.rsvp.notifications` (couple alerts), `wedding.audit.events` (NoSQL audit logging).
- **Behavior:** Transactional publishing from Spring Boot services with dead-letter queue handling for resilient delivery.

### Security, Rate Limiting & IDOR Prevention
- Stateless JWT authentication + OAuth2 Social Login.
- Server-side RBAC and side-permission checks on all protected endpoints.
- Rate limiting middleware on `/api/v1/auth/*` and `/api/v1/public/*` returning `429`.

### Delivery & Pull Request Workflow
- Development on feature branches (`feature/<phase>-<description>`).
- Final pull request (PR) submitted to `main` branch upon phase completion.
- Containerized deployment with Docker Compose orchestrating PostgreSQL, MongoDB, Redis, RabbitMQ, Spring Boot API, and Nginx UI.

---

## 2. Roles and Access

| Role | Access | Purpose |
| :--- | :--- | :--- |
| **Bride** | Bride private + Shared | Bride planning and collaboration |
| **Groom** | Groom private + Shared | Groom planning and collaboration |
| **Bride-side Support** | Permitted Bride-side records | Assist bride-side planning |
| **Groom-side Support** | Permitted Groom-side records | Assist groom-side planning |
| **Guest** | Invitation + RSVP only | View invitation and respond |
| **Administrator** | System-wide administration | Accounts, templates, configuration and monitoring |

> **Security Requirement:** Authorization must be enforced by the backend. Hiding a page in the frontend is NOT a security control.

---

## 3. Main Navigation

| Module | Purpose |
| :--- | :--- |
| **Overview** | Progress, tasks, budget, guests and deadlines |
| **My Planning** | Private bride-side or groom-side planning |
| **Shared Wedding** | Common wedding activities |
| **Tasks** | Authorized tasks |
| **Budget & Expenses** | Budgets and actual spending |
| **Guests & RSVP** | Guest list and attendance |
| **Seating** | Tables and guest assignments |
| **Timeline** | Preparation milestones |
| **Vendors** | Vendor information and costs |
| **Home Preparation** | New-home preparation |
| **Invitations** | Digital invitations and RSVP tracking |

---

## 4. Core Functional Requirements
- Wedding event creation and partner association.
- One supporting family account for each side.
- Private bride-side and groom-side planning.
- Shared planning for venue, food, drinks, decoration, entertainment, photography, invitations, and other common activities.
- Task creation, assignment, deadlines, priorities, and statuses.
- Wedding checklist and timeline.
- Budget creation by category and responsibility.
- Expense recording and planned-vs-actual tracking.
- Dashboard progress summaries.
- Customizable wedding planning templates.
- Customizable new-home preparation templates.
- Digital invitations.
- Guest list and RSVP management.
- Basic seating management.
- Vendor management.
- Role-based visibility and authorization.

---

## 5. Home Preparation

| Bride-side Examples | Groom-side Examples |
| :--- | :--- |
| Plates, spoons, cups | House rent |
| Kitchen items | Refrigerator |
| Bed sheets | TV |
| Curtains | Sofa / bed |
| Household items | Dining table / appliances |

### Functionalities:
- Add, edit, and remove items.
- Assign an item to a person.
- Set budget and deadline.
- Mark complete.
- Add custom items/categories.
- Template items must remain editable.

---

## 6. Task Management
- **Task Fields:** Title, category, assignee, due date, priority, status, budget, and notes.
- **Statuses:** `Not Started`, `In Progress`, `Completed`, `Overdue`.
- **Assignees:** Depend on visibility: Bride, Groom, Bride-side Support, or Groom-side Support.
- **Security:** Users only see and modify tasks permitted by their role/side.

---

## 7. Budget and Expenses
- Total planned budget.
- Total spent.
- Remaining amount.
- Category breakdown.
- Planned vs actual comparison.
- Recent expenses.
- RWF formatting.
- Private financial records must follow the same side-based authorization rules.

---

## 8. Guests, Invitations and Seating
- Create, search, and filter guests.
- Track status: `Confirmed`, `Pending`, and `Declined`.
- Create and share digital invitations.
- Guest views invitation and confirms/declines attendance.
- Couple monitors RSVP responses.
- Create tables and capacities.
- Assign/move guests between tables.
- Show unassigned guests and capacity conflicts.

---

## 9. Vendors and Timeline
- **Vendor Categories:** venue, catering, decoration, entertainment, photography/videography, beauty, transport, and custom.
- **Vendor Fields:** name, category, contact, cost, booking status, payment status, and notes.
- **Timeline:** Supports milestones, dates, deadlines, upcoming, and overdue items.

---

## 10. UI/UX Design System
The interface should look like a premium planning/control application, not a wedding invitation website.

| Element | Direction |
| :--- | :--- |
| **Primary** | Deep Burgundy / Wine |
| **Secondary** | Muted Champagne / Warm Gold |
| **Background** | Warm Ivory / Off-white |
| **Surface** | White / Warm White |
| **Text** | Deep Charcoal |
| **Success** | Muted Green |
| **Warning** | Warm Amber |
| **Error** | Muted Red |
| **Headings** | Playfair Display or DM Serif Display |
| **UI / Body** | Inter or Manrope |

### Styling Guidelines:
- Use gold sparingly.
- Use medium-radius cards, thin borders, and subtle shadows.
- Use clean tables, badges, progress indicators, and Lucide-style line icons.
- Avoid excessive pink, flowers, hearts, gradients, glassmorphism, and decorative UI.
- **Desktop:** Persistent sidebar and multi-column dashboard.
- **Mobile:** Collapsible sidebar/bottom navigation and mobile-friendly tables/forms.
- Clearly label Private, Shared, Bride-side, and Groom-side information.

---

## 11. Dashboard
- Wedding name and date.
- Overall completion percentage.
- Tasks completed/remaining/overdue.
- Planned budget, spent, and remaining.
- Confirmed/pending/declined guests.
- Upcoming deadlines.
- Recent expenses.
- Quick actions: Add Task, Add Expense, Add Guest, Add Vendor.
- Show the user's current side clearly.

---

## 12. Core Database Entities
- **User:** Account/profile
- **Wedding:** Central wedding event
- **WeddingMember:** User, role and side within a wedding
- **PlanningCategory:** Planning categories
- **Task:** Planning responsibility
- **TaskAssignment:** Task assignment
- **Budget:** Planned financial allocation
- **Expense:** Actual spending
- **TimelineItem:** Milestones/deadlines
- **Vendor:** Service provider
- **HomePreparationItem:** Household preparation
- **PlanningTemplate / TemplateItem:** Reusable starter plans
- **Guest:** Guest record
- **Invitation:** Digital invitation
- **RSVP:** Attendance response
- **Table / TableAssignment:** Seating
- **Notification:** User alerts
- **ActivityLog:** Important/audit activity

---

## 13. Backend Module Boundaries
- Auth
- Users
- Weddings
- Members / Roles
- Planning / Tasks
- Templates
- Budgets
- Expenses
- Timeline
- Vendors
- Guests
- Invitations
- RSVP
- Seating
- Notifications
- Admin

Each module should own its validation and business rules. Keep authorization checks close to protected operations.

---

## 14. Key Business Rules
- One wedding has one bride, one groom and at most one supporting account per side under the current scope.
- A supporting account must belong to the correct side.
- Every protected record has an explicit visibility scope.
- Negative expense amounts are invalid.
- Financial calculations must be performed reliably on the server.
- A guest cannot be assigned to multiple tables simultaneously.
- Table capacity conflicts must be prevented or clearly reported.
- RSVP responses belong to a guest/invitation.
- Applying a template creates editable records; it does not lock the user into the template.
- Private records must never be returned by unauthorized APIs.

---

## 15. User Story Backlog

| Actor | User-story capabilities |
| :--- | :--- |
| **Bride** | Create wedding; invite groom; add bride-side support; manage private tasks; set deadlines; record expenses; manage home preparation; access shared planning. |
| **Groom** | Join wedding; add groom-side support; manage private tasks; set deadlines; record expenses; manage home preparation; access shared planning. |
| **Bride Support** | Access assigned wedding; view permitted bride-side tasks; update assigned tasks; view permitted expenses; see bride-side progress. |
| **Groom Support** | Access assigned wedding; view permitted groom-side tasks; update assigned tasks; view permitted expenses; see groom-side progress. |
| **Shared** | View shared activities; manage venue; food/drinks; decoration/entertainment; shared budget; shared expenses; shared progress. |
| **Templates** | Use wedding template; use home template; customize items; remove items; add custom items. |
| **Finance** | Create budget; categorize expenses; record actuals; compare planned/actual; view remaining budget; view summaries. |
| **Guests** | Create invitation; share invitation; view invitation; RSVP; view responses; manage guest list; manage seating. |
| **Admin** | Manage accounts; templates; reported content; system activity; configuration. |

---

## 16. Testing Requirements
- Unit tests for business logic and financial calculations.
- Integration/API tests for authorization boundaries.
- Bride-private isolation tests.
- Groom-private isolation tests.
- Supporting-member permission tests.
- Shared-data access tests.
- Budget/expense calculation tests.
- RSVP and seating tests.
- Template customization tests.
- Responsive testing for critical mobile workflows.
- Regression tests for important bug fixes.

---

## 17. Development Phases

| Phase | Deliverables |
| :--- | :--- |
| **1. Foundation** | Project setup, authentication, users, roles, wedding creation, membership and authorization |
| **2. Planning Core** | Categories, tasks, assignments, deadlines, statuses and dashboard progress |
| **3. Templates** | Wedding and home-preparation templates |
| **4. Budget** | Budgets, expenses, categories and financial summaries |
| **5. Shared Wedding** | Venue, catering, drinks, decoration, entertainment and photography |
| **6. Guests & Invitations** | Guest list, digital invitations and RSVP |
| **7. Seating** | Tables, capacities and assignments |
| **8. Vendors & Timeline** | Vendor records and preparation milestones |
| **9. Notifications & Admin** | Notifications, activity records and administration |
| **10. Quality & Release** | Security, authorization, responsive, performance and deployment testing |

---

## 18. Development Workflow
1. Define database model.
2. Define authorization rules.
3. Implement backend endpoint + validation.
4. Write backend tests.
5. Build frontend page/components.
6. Connect frontend to API.
7. Handle loading, empty and error states.
8. Test the complete workflow.
9. Move to the next feature.

---

## 19. Definition of Done
- Requirement implemented.
- Backend authorization verified.
- Validation implemented.
- Important business rules tested.
- Frontend handles loading/success/empty/error states.
- Responsive layout verified.
- Financial calculations verified.
- No unauthorized private data returned.
- Feature manually tested end-to-end.
- Important defects have regression tests.

---

## 20. MVP Priority
Build the core workflow first:
- Authentication and role-based access.
- Wedding creation and partner association.
- Bride/groom private areas.
- Supporting family accounts.
- Shared wedding planning.
- Tasks and deadlines.
- Home preparation.
- Budget and expense tracking.
- Guest list and RSVP.
- Basic digital invitations.
- Dashboard progress.

*Advanced vendor management, seating enhancements, notifications, and administrative features can be expanded after the core workflow is stable.*

---

## 21. Product Differentiator
The key product idea is **controlled collaboration**: the bride and groom can work together on shared wedding activities while keeping side-specific responsibilities, expenses, and home preparation private. The system should remain flexible enough for different Rwandan wedding practices.
