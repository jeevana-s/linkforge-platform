# LinkForge API

Base URL: `http://localhost:8081`
Auth: `Authorization: Bearer <accessToken>`

## Auth
POST /api/auth/register { email, password, name } → { accessToken, refreshToken, email, role }
POST /api/auth/login    { email, password }       → tokens
POST /api/auth/refresh  { refreshToken }          → tokens

## URLs
POST   /api/urls                  { originalUrl, customAlias?, expiresAt?, password?, oneTime? }
GET    /api/urls?page=0&size=20
DELETE /api/urls/{id}
GET    /api/urls/{id}/qr          → image/png

## Analytics
GET /api/analytics/{id}        → totalClicks, uniqueClicks, byBrowser, byCountry, byDevice, timeline
GET /api/analytics/summary     → dashboard summary

## Redirect (public)
GET /{code}?password=...       → 302 Found

## Admin (ROLE_ADMIN)
GET  /api/admin/users
POST /api/admin/users/{id}/disable
GET  /api/admin/urls
POST /api/admin/urls/{id}/block
GET  /api/admin/stats

Full schema at /swagger-ui.html.
