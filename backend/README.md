# Hospital Management System - Backend

## Project setup

Configured from the requested IntelliJ Spring Initializr settings:

- Name: backend
- Group: com.utkarsh2573
- Artifact: backend
- Package: com.utkarsh2573.backend
- Java: 21
- Maven
- Jar
- YAML configuration

## Phase 1 includes

- Spring Boot REST API
- MySQL
- Spring Data JPA
- Spring Security
- JWT authentication
- BCrypt password hashing
- Role-based authorization
- CORS for React/Vite
- Health endpoint
- Seed users for all HMS roles

## Roles

- ADMIN
- RECEPTIONIST
- DOCTOR
- PHARMACIST
- LAB_TECHNICIAN
- PATIENT

## Before running

1. Install MySQL 8.
2. Make sure MySQL is running.
3. Change `spring.datasource.username` and `spring.datasource.password`
   in `src/main/resources/application.yml`.
4. Change `app.jwt.secret` to a long random secret.
5. Run:

```bash
mvn clean spring-boot:run
```

## Seed credentials

Development only:

| Role | Username | Password |
|---|---|---|
| ADMIN | admin | Admin@123 |
| RECEPTIONIST | reception | Reception@123 |
| DOCTOR | doctor | Doctor@123 |
| PHARMACIST | pharmacy | Pharmacy@123 |
| LAB_TECHNICIAN | lab | Lab@123 |
| PATIENT | patient | Patient@123 |

Change/remove these credentials before production.

## Authentication

POST:

`/api/v1/auth/login`

Request:

```json
{
  "username": "doctor",
  "password": "Doctor@123"
}
```

The response contains a JWT and role.

Use:

`Authorization: Bearer <token>`

## Test role access

- `/api/v1/admin/dashboard`
- `/api/v1/receptionist/dashboard`
- `/api/v1/doctor/dashboard`
- `/api/v1/pharmacy/dashboard`
- `/api/v1/lab/dashboard`
- `/api/v1/patient/dashboard`

Only the appropriate role can access each endpoint.

## Next phase

Phase 2 will implement:

- Patient entity and CRUD
- Doctor and department entities
- Visit entity
- Queue generation
- Receptionist patient registration
- Consultation billing
- Payment records
