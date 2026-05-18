# LinkForge — Distributed Smart URL Shortening Platform

A production-grade, distributed URL shortening SaaS inspired by Bitly/TinyURL.

> Spring Boot 3 · Java 17 · PostgreSQL · Redis · Kafka · React 18 · Tailwind · Docker

---

## ✨ Features

- 🔐 JWT auth (access + refresh tokens, BCrypt passwords)
- 🔗 URL shortening with custom aliases, QR codes, expiry, password-protected & one-time links
- ⚡ Redis-cached redirects for sub-millisecond hot-path latency
- 📊 Analytics: total/unique clicks, browser/device, country, time-series charts
- 🚦 IP-based rate limiting (Bucket4j) + API throttling
- 🛡️ Spam/malicious URL detection, admin moderation
- 📨 Kafka async pipeline for click events
- 🧑‍💼 Admin panel: users, URLs, traffic stats
- 🎨 Premium glassmorphism UI, dark/light mode, Framer Motion, Recharts
- 📦 One-command Docker Compose deploy
- 📑 Swagger / OpenAPI docs at `/swagger-ui.html`

---

## 🏗️ Architecture

```
┌──────────┐    HTTPS     ┌──────────────────┐
│  React   │ ───────────► │  Spring Boot API │
│ Frontend │ ◄─────────── │  (Stateless JWT) │
└──────────┘              └────────┬─────────┘
                                   │
            ┌──────────────────────┼──────────────────────┐
            ▼                      ▼                      ▼
       ┌─────────┐           ┌──────────┐           ┌──────────┐
       │  Redis  │           │ Postgres │           │  Kafka   │
       │ (cache) │           │   (DB)   │           │ (events) │
       └─────────┘           └──────────┘           └────┬─────┘
                                                         ▼
                                                  ┌─────────────┐
                                                  │ Analytics   │
                                                  │ Consumer    │
                                                  └─────────────┘
```

**Redirect path (hot)**: `GET /{code}` → Redis lookup → 302 redirect → fire-and-forget Kafka event.
**Cache miss**: DB lookup → populate Redis (TTL) → 302.

---

## 🧰 Tech Stack

| Layer        | Tech                                       |
|--------------|--------------------------------------------|
| Frontend     | React 18, Vite, Tailwind, Framer Motion, Recharts, Axios, React Router |
| Backend      | Java 17, Spring Boot 3, Spring Security, JPA/Hibernate, Maven |
| Database     | PostgreSQL 15 (indexed)                    |
| Cache        | Redis 7                                    |
| Queue        | Apache Kafka                               |
| Rate Limit   | Bucket4j                                   |
| Auth         | JWT (jjwt 0.12)                            |
| Docs         | springdoc-openapi (Swagger UI)             |
| DevOps       | Docker, docker-compose                     |

---

## 🚀 Quickstart

```bash
git clone <repo>
cd linkforge
cp .env.example .env
docker-compose up --build
```

- Frontend: http://localhost:3000
- Backend:  http://localhost:8081
- Swagger:  http://localhost:8081/swagger-ui.html

### Local dev (no Docker)

**Backend**
```bash
cd backend
./mvnw spring-boot:run
```
Requires local Postgres on `5432`, Redis on `6379`, Kafka on `9092`.

**Frontend**
```bash
cd frontend
npm install
npm run dev
```

---

## 🗄️ PostgreSQL Setup

```sql
CREATE DATABASE linkforge;
CREATE USER linkforge WITH PASSWORD 'linkforge';
GRANT ALL PRIVILEGES ON DATABASE linkforge TO linkforge;
```
Hibernate auto-creates tables (`spring.jpa.hibernate.ddl-auto=update`). Indexes are declared in the entity annotations.

## 🧠 Redis Setup

```bash
docker run -p 6379:6379 redis:7-alpine
```
Used for:
- `url:{code}` → original URL (TTL 1h)
- `ratelimit:{ip}` → token bucket

## 📡 API Documentation

| Method | Endpoint                     | Description                  | Auth |
|--------|------------------------------|------------------------------|------|
| POST   | `/api/auth/register`         | Register user                | ❌   |
| POST   | `/api/auth/login`            | Login → JWT                  | ❌   |
| POST   | `/api/auth/refresh`          | Refresh access token         | ❌   |
| POST   | `/api/urls`                  | Create short URL             | ✅   |
| GET    | `/api/urls`                  | List my URLs (paginated)     | ✅   |
| DELETE | `/api/urls/{id}`             | Delete URL                   | ✅   |
| GET    | `/api/urls/{id}/qr`          | QR code PNG                  | ✅   |
| GET    | `/api/analytics/{id}`        | Per-link analytics           | ✅   |
| GET    | `/api/analytics/summary`     | Dashboard summary            | ✅   |
| GET    | `/{code}`                    | Redirect (public)            | ❌   |
| GET    | `/api/admin/users`           | List users                   | 👑   |
| POST   | `/api/admin/urls/{id}/block` | Block malicious URL          | 👑   |

Full schema & request/response samples at `/swagger-ui.html`.

---

## 📈 Scaling Notes

- **Stateless backend** → horizontally scalable behind a load balancer.
- **Redis cache** absorbs 95%+ of redirect traffic.
- **Kafka** decouples click ingest from redirect path (zero added latency).
- **DB indexes** on `short_code`, `user_id`, `(url_id, clicked_at)`.
- **Bucket4j** prevents abuse without round-trips.

---

## 🌐 Deployment

- **Frontend** → Vercel (`vercel --prod` inside `frontend/`)
- **Backend** → Render / Railway / Fly.io (Dockerfile included)
- Set env vars from `.env.example` in the dashboard.

---

## 📝 License
MIT
