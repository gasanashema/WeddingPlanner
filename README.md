# WedPlan - Rwanda Collaborative Wedding Planning Platform

**WedPlan** is a web platform designed to centralize wedding planning in Rwanda. It features a central wedding unit with distinct bride-side, groom-side, and shared planning areas, supporting family member accounts, task tracking, budgeting in RWF, home preparation, digital invitations, RSVP, seating management, and vendor tracking.

---

## 🌟 Key Features

- **Side Isolation & Privacy:** Strict backend-enforced separation between Bride-side private data, Groom-side private data, and Shared planning activities.
- **Role-Based Access Control:** Roles for Bride, Groom, Bride Support, Groom Support, Guests, and Administrator.
- **Task & Timeline Management:** Tasks with priorities, assignees, due dates, and customizable Rwandan starter templates.
- **Home Preparation:** Separate tracking for household items (appliances, furniture, kitchenware).
- **Budget & Expenses:** Financial allocations and actual expense recording in **Rwandan Francs (RWF)**.
- **Digital Invitations & RSVP:** Custom shareable invitation links with public unauthenticated RSVP submission.
- **Seating Management:** Table creation, capacity conflict warnings, and guest table assignment.

---

## 🛠 Tech Stack

- **Backend:** Java 21, Spring Boot 3.3.4, Spring Security, Spring Data JPA, JWT Authentication, PostgreSQL / H2 Database, Maven.
- **Frontend:** React 18, TypeScript, Vite, TailwindCSS, Lucide React Icons.
- **DevOps & Containers:** Docker, Docker Compose, Nginx.

---

## 🚀 How to Start the Project

### Prerequisites
Make sure you have the following installed on your system:
- [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)
- [Java 21 JDK](https://adoptium.net/) (for local backend development)
- [Node.js 20+](https://nodejs.org/) & `npm` (for local frontend development)

---

### Option 1: Run Full Application with Docker Compose (Recommended)

Run the entire stack (PostgreSQL Database, Spring Boot API, and Nginx Frontend) with a single command:

```bash
# Clone repository and navigate to root directory
git clone https://github.com/gasanashema/WeddingPlanner.git
cd Wedding-System

# Start all services with Docker Compose
docker compose up --build
```

#### Access Points:
- 🌐 **Frontend UI:** `http://localhost:3000`
- ⚡ **Backend API:** `http://localhost:8080/api/v1`
- 🗄 **PostgreSQL Database:** `localhost:5432` (`wedplandb`)

To stop the Docker environment:
```bash
docker compose down
```

---

### Option 2: Run Backend and Frontend Locally

#### 1. Run the Backend API (Spring Boot)

```bash
cd weddingPlanner-backend

# Compile and run Spring Boot application
./mvnw spring-boot:run
```
> The backend runs on `http://localhost:8080` (H2 database enabled in-memory for quick local testing at `http://localhost:8080/h2-console`).

#### 2. Run the Frontend UI (React + Vite)

```bash
cd WeddingPlanner-frontend

# Install dependencies
npm install

# Start Vite development server
npm run dev
```
> The frontend runs on `http://localhost:5173` (or port specified by Vite).

---

## 🧪 Running Tests

### Run Backend Unit & Integration Tests
```bash
cd weddingPlanner-backend
./mvnw test
```

### Run Frontend Build Check
```bash
cd WeddingPlanner-frontend
npm run build
```

---

## 📁 Repository Structure

```
Wedding-System/
├── docker-compose.yml              # Root Docker Compose orchestrating all services
├── requirements.md                 # Full Product Development Specification
├── implementation_plan.md          # 11-Phase Granular Implementation Roadmap
├── history.md                      # Session progress & continuity tracking
├── weddingPlanner-backend/         # Spring Boot 3 Backend REST API
│   ├── Dockerfile                  # Backend multi-stage Docker build
│   ├── pom.xml                     # Maven project configuration
│   └── src/                        # Java source code & tests
└── WeddingPlanner-frontend/        # React 18 + TypeScript Vite Frontend
    ├── Dockerfile                  # Frontend Nginx production container
    ├── nginx.conf                  # Nginx SPA fallback & API proxying
    ├── package.json                # Frontend dependencies & scripts
    └── src/                        # React components, pages & contexts
```

---

## 🔒 Security & Authorization Rules

1. **Backend Authorization:** Security is strictly enforced on the server via JWT bearer tokens. Frontend view filters are for UX only.
2. **Private Scopes:** `BRIDE_PRIVATE` records are never returned to Groom users, and `GROOM_PRIVATE` records are never returned to Bride users.
3. **No Negative Expenses:** Financial endpoints enforce positive monetary inputs on the server.
