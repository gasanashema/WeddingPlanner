# UBUKWE PLATFORM - EXTERNAL SERVICES & API KEYS GUIDE

> **Living Document:** This guide provides step-by-step instructions for acquiring, configuring, and managing credentials, API keys, and environment variables for external services integrated into the Ubukwe Wedding Management System.
> 
> As new phases and integrations are added to the platform, this document is updated with exact setup procedures.

---

## 📋 Summary of Required Production Environment Variables

| Service Category | Environment Variable Name | Required Phase | Description / Default Fallback |
| :--- | :--- | :--- | :--- |
| **Google OAuth2** | `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID` | Phase 3 | Google Cloud OAuth2 Client ID |
| **Google OAuth2** | `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET` | Phase 3 | Google Cloud OAuth2 Client Secret |
| **GitHub OAuth2** | `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_ID` | Phase 3 | GitHub OAuth App Client ID |
| **GitHub OAuth2** | `SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET` | Phase 3 | GitHub OAuth App Client Secret |
| **MongoDB** | `SPRING_DATA_MONGODB_URI` | Phase 3 | Connection string (default: `mongodb://localhost:27017/wedplan_db`) |
| **Redis** | `SPRING_DATA_REDIS_HOST` | Phase 3 | Hostname (default: `localhost`) |
| **Redis** | `SPRING_DATA_REDIS_PORT` | Phase 3 | Port (default: `6379`) |
| **Redis** | `SPRING_DATA_REDIS_PASSWORD` | Phase 3 | Password (optional for local dev) |
| **MoMo Webhook** | `MOMO_WEBHOOK_SECRET` | Phase 7 | Mobile Money Webhook Signature Key |
| **SMTP Email** | `SPRING_MAIL_HOST`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD` | Phase 8 | SMTP Provider Credentials |
| **Twilio SMS** | `TWILIO_ACCOUNT_SID`, `TWILIO_AUTH_TOKEN` | Phase 8 | Twilio SMS API Credentials |

---

## 1. Google OAuth2 Social Login Setup

### Purpose
Allows couples, partners, guests, and support accounts to log in using their Google accounts.

### Step-by-Step Acquisition Guide:
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Log in with your Google Account and click **Select a project** > **New Project**.
3. Name your project (e.g., `Ubukwe-Wedding-Platform`) and click **Create**.
4. In the left sidebar, navigate to **APIs & Services** > **OAuth consent screen**.
5. Select **External** user type and click **Create**.
6. Fill in the required app info:
   - **App name:** `Ubukwe Wedding Planner`
   - **User support email:** Your email address.
   - **Developer contact information:** Your email address.
7. Click **Save and Continue** through Scopes (default `email`, `profile`, `openid` are sufficient).
8. Navigate to **APIs & Services** > **Credentials**.
9. Click **+ CREATE CREDENTIALS** > **OAuth client ID**.
10. Select **Application type:** `Web application`.
11. Set **Authorized redirect URIs**:
    - For Local Development: `http://localhost:8080/login/oauth2/code/google`
    - For Production: `https://your-production-domain.com/login/oauth2/code/google`
12. Click **Create**. Copy your **Client ID** and **Client Secret**.

### Spring Boot Configuration:
Set environment variables:
```bash
export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID="your-client-id.apps.googleusercontent.com"
export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET="your-client-secret"
```

---

## 2. GitHub OAuth2 Social Login Setup

### Purpose
Allows users to log in using their GitHub accounts.

### Step-by-Step Acquisition Guide:
1. Log in to [GitHub](https://github.com/).
2. In the upper-right corner, click your profile photo > **Settings**.
3. In the left sidebar, scroll down and click **Developer settings**.
4. Click **OAuth Apps** > **New OAuth App**.
5. Fill out the application details:
   - **Application name:** `Ubukwe Wedding Platform`
   - **Homepage URL:** `http://localhost:8080` (or production domain)
   - **Authorization callback URL:** `http://localhost:8080/login/oauth2/code/github` (or production callback URL)
6. Click **Register application**.
7. Copy the **Client ID**.
8. Click **Generate a new client secret**, copy the generated secret immediately.

### Spring Boot Configuration:
Set environment variables:
```bash
export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_ID="your-github-client-id"
export SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET="your-github-client-secret"
```

---

## 3. MongoDB (NoSQL Operational Audit & Activity Logs)

### Purpose
Stores unstructured operational activity streams, system audit logs, and digital invitation view metrics.

### Local Development Setup (Default):
Managed automatically via Docker Compose:
```bash
docker compose up -d wedplan_mongo
```

### Production Setup (MongoDB Atlas Cloud):
1. Sign up at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas).
2. Create a Cluster (Free M0 or dedicated M10+ instance).
3. Navigate to **Database Access** > **Add New Database User** (create username & strong password).
4. Navigate to **Network Access** > **Add IP Address** (add your production server IP or `0.0.0.0/0` for cloud PaaS).
5. Click **Database** > **Connect** > **Drivers** to get your connection URI.
6. Connection string format:
   `mongodb+srv://<username>:<password>@cluster0.xxx.mongodb.net/wedplan_db?retryWrites=true&w=majority`

### Spring Boot Configuration:
```bash
export SPRING_DATA_MONGODB_URI="mongodb+srv://<username>:<password>@cluster0.xxx.mongodb.net/wedplan_db"
```

---

## 4. Redis (NoSQL Session Revocation & Rate Limiting Cache)

### Purpose
Manages blacklisted JWT tokens on logout and acts as a high-performance memory store for rate limiting counters.

### Local Development Setup (Default):
Managed automatically via Docker Compose:
```bash
docker compose up -d wedplan_redis
```

### Production Setup (Redis Cloud / AWS ElastiCache):
1. Sign up at [Redis Cloud](https://redis.com/try-free/) or provision AWS ElastiCache for Redis.
2. Obtain Hostname, Port, and Password.

### Spring Boot Configuration:
```bash
export SPRING_DATA_REDIS_HOST="your-redis-host.redislabs.com"
export SPRING_DATA_REDIS_PORT="14521"
export SPRING_DATA_REDIS_PASSWORD="your-redis-password"
```

---

## 5. Mobile Money (MoMo) Webhook Integration (Planned Phase 7)

### Purpose
Processes direct RSVP gift contributions and expense settlements via MTN MoMo / Airtel Money.

### Acquisition Guide (To be finalized in Phase 7):
1. Register on the [MTN MoMo Developer Portal](https://momodeveloper.mtn.com/).
2. Provision API User ID and API Secret Key.
3. Configure Webhook Listener URL: `https://api.ubukwe.rw/api/v1/public/contributions/webhook`.

---

## 6. RabbitMQ, SMTP & SMS Gateway Setup (Planned Phase 8)

### Purpose
Handles asynchronous queuing for digital invitation emails, SMS reminders, and push notifications.

### Acquisition Guide (To be finalized in Phase 8):
- **RabbitMQ:** Docker container default (`rabbitmq:3-management`) or CloudAMQP.
- **SMTP Email:** SendGrid / Amazon SES / Mailgun API key setup.
- **SMS Gateway:** Twilio Account SID & Auth Token or Africa's Talking API key setup.
