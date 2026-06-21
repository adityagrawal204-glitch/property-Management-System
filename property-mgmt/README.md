# Property Management System

A full-stack property management app: **Spring Boot REST API** with **Spring Security (JWT)**, **JPA entity relationships**, and a **plain HTML/CSS/JS frontend**.

Built as a portfolio project — demonstrates entity relationships, authentication, custom aggregation queries, and a real business rule (occupancy tracking), not just basic CRUD.

## Data model

```
User (landlord account)
  └── Property (1-to-many: a user owns many properties)
        └── Unit (1-to-many: a property has many units)
              └── Tenant (1-to-1: a unit has at most one current tenant)
```

Every Property and Unit is scoped to the logged-in User — one landlord can never see or modify another's data, enforced at the repository/service layer on every request.

## Features

- **Auth**: signup/login with BCrypt password hashing, stateless JWT tokens
- **Properties**: full CRUD, scoped per owner
- **Units**: full CRUD, nested under properties, tracks bedrooms/bathrooms/rent/status
- **Tenants**: move-in / move-out actions that automatically flip unit status (VACANT ↔ OCCUPIED)
- **Search**: filter units by city and/or rent range (`/api/units/search?city=austin&minRent=1000&maxRent=2000`)
- **Occupancy Dashboard**: aggregated stats — total/occupied/vacant units, occupancy rate %, total rent currently collected vs potential rent if fully occupied
- **Tests**: integration tests covering signup, auth rejection, and the full create-property flow

## Project structure

```
src/main/java/com/example/propertymgmt/
├── model/        Entities: User, Property, Unit, Tenant, UnitStatus
├── repository/    Spring Data JPA interfaces (incl. custom JPQL search query)
├── service/       Business logic, ownership checks, dashboard aggregation
├── controller/    REST endpoints
├── security/      JWT generation/validation, Spring Security UserDetailsService
├── config/        SecurityConfig (filter chain, CORS), GlobalExceptionHandler
└── dto/           Request/response shapes (keeps API contracts separate from entities)
frontend/index.html   Single-file frontend: login, properties, units, dashboard
```

## Running it

### Backend

By default this uses **H2 in-memory** so you can run it immediately with zero setup:

```bash
cd property-mgmt
mvn spring-boot:run
```

You'll see `Tomcat started on port(s): 8080` when it's ready.

**To switch to real MySQL** (recommended before putting this on your resume — "MySQL" in your tech stack should mean you actually ran MySQL):
1. Install MySQL locally, or use a free hosted instance (Railway, PlanetScale, etc.)
2. Create a database: `CREATE DATABASE property_mgmt;`
3. In `src/main/resources/application.properties`, comment out the H2 block and uncomment the MySQL block, filling in your password
4. Run again — Hibernate will auto-create all the tables on startup

### Frontend

Open `frontend/index.html` directly in your browser. No build step.

### Trying it out

1. Sign up with any email/password (min 6 characters)
2. Add a property (name, address, city)
3. Click into it, add a unit (unit number, beds/baths, rent)
4. Click "Move in tenant" — watch the dashboard occupancy stats update
5. Try the city filter on the properties list

## Running the tests

```bash
mvn test
```

Covers: signup flow, JWT-protected endpoint rejecting unauthenticated requests, and full create+fetch property flow with a real token.

## API reference

| Method | Endpoint | Auth required | Description |
|---|---|---|---|
| POST | `/api/auth/signup` | No | Create account, returns JWT |
| POST | `/api/auth/login` | No | Returns JWT |
| GET | `/api/properties` | Yes | List your properties |
| POST | `/api/properties` | Yes | Create a property |
| PUT | `/api/properties/{id}` | Yes | Update a property |
| DELETE | `/api/properties/{id}` | Yes | Delete a property (cascades to its units) |
| GET | `/api/properties/search?city=` | Yes | Filter properties by city |
| GET | `/api/properties/{id}/units` | Yes | List units in a property |
| POST | `/api/properties/{id}/units` | Yes | Add a unit |
| PUT/DELETE | `/api/units/{id}` | Yes | Update/delete a unit |
| GET | `/api/units/search?city=&minRent=&maxRent=` | Yes | Filter units across all your properties |
| POST | `/api/units/{id}/tenant/move-in` | Yes | Assign a tenant, sets unit to OCCUPIED |
| POST | `/api/units/{id}/tenant/move-out` | Yes | Remove tenant, sets unit to VACANT |
| GET | `/api/dashboard/occupancy` | Yes | Aggregated occupancy stats |

All authenticated requests need an `Authorization: Bearer <token>` header.

## What to study, mapped to files (since you're learning Spring Boot now)

1. **`model/`** — JPA relationship annotations: `@OneToMany`/`@ManyToOne` (Property↔Unit, User↔Property) and `@OneToOne` (Unit↔Tenant). Pay attention to which side has `mappedBy` (the "inverse" side) vs `@JoinColumn` (the "owning" side, which holds the actual foreign key).
2. **`security/JwtUtil.java`** + **`JwtAuthFilter.java`** — how a stateless token-based auth flow works end to end: generate on login, verify on every subsequent request, no server-side session.
3. **`config/SecurityConfig.java`** — the security filter chain: which routes are public (`/api/auth/**`) vs protected, BCrypt setup, stateless session policy.
4. **`service/TenantService.java`** — `moveIn`/`moveOut` show a real business rule enforced in the service layer: a tenant and a unit's status must change together, atomically, inside one transaction (JPA's "dirty checking" persists the Unit's status change automatically alongside the Tenant save).
5. **`repository/UnitRepository.java`** — the custom `@Query` with optional filters (`:param IS NULL OR ...` pattern) is a common real-world technique for building flexible search endpoints without an if/else explosion.
6. **`service/DashboardService.java`** — aggregating data across a relationship (summing rent across all Units under all Properties for one owner) rather than just listing rows from one table.
7. **`config/GlobalExceptionHandler.java`** — `@RestControllerAdvice` centralizes error handling so controllers stay clean and every error response has a consistent JSON shape.

## Notes on what's intentionally simplified

This was scoped for a weekend build, so a few things are deliberately minimal — worth knowing for if you extend it or get asked about it in an interview:
- **Single role** (`ROLE_USER`) — no admin/tenant-login distinction yet. Adding tenant-facing accounts with restricted permissions would be a natural next step.
- **No lease history** — moving out a tenant deletes the Tenant record entirely rather than archiving it. A `LeaseHistory` table would be needed for a "past tenants" feature.
- **No pagination** — property/unit lists return everything at once. Fine at small scale, would need `Pageable` for a landlord with hundreds of units.
- **Permissive CORS** (`*`) — fine for local development, should be restricted to a specific frontend origin before any real deployment.
