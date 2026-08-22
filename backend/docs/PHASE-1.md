# Phase 1 - Backend Foundation

## Architecture

Controller -> Service -> Repository -> Entity -> MySQL

Security:

Request -> JWT Filter -> SecurityContext -> Role Authorization -> Controller

## Current package layout

com.utkarsh2573.backend
├── auth
├── common
├── config
├── dashboard
├── security
└── user

## Important rule

The frontend may hide screens based on role, but the backend remains the
source of truth for authorization.

## Phase 1 success criteria

- Application starts on port 8080.
- MySQL connection works.
- Seed users are created.
- Login returns a JWT.
- Protected endpoints reject requests without JWT.
- Role-specific endpoints reject incorrect roles.
