# CarFlow – Car Rental REST API

![CI](https://github.com/wzxcff/CarRental/actions/workflows/ci.yml/badge.svg)
![Java Version](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen.svg)

CarFlow is a backend RESTful API service for daily vehicle rentals across fixed station locations (e.g., airports, train stations). The system features Role-Based Access Control (RBAC) supporting Users, Mechanics, and Administrators, integrated date-range availability searching, and maintenance tracking.

## Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot (Spring Web, Spring Security, Spring Data JPA, Validation, Actuator)
* **Database & Migrations:** PostgreSQL, Flyway
* **Testing:** Testcontainers, JUnit 5, MockMvc
* **Documentation & Tooling:** OpenAPI 3 / Swagger UI (`springdoc-openapi`), Maven, Docker, GitHub Actions (CI)
* **Architecture:** Package by Feature

## Key Features & Role Matrix

| Role | Key Capabilities |
| :--- | :--- |
| **User (`ROLE_USER`)** | • Account registration & JWT authentication<br>• Search available cars by date interval, car type, and station location<br>• Sort car search results by daily price<br>• Book vehicles with designated pickup and return locations<br>• View personal rental history |
| **Mechanic (`ROLE_MECHANIC`)** | • Search vehicle records by license plate<br>• Toggle vehicle status (`AVAILABLE` ↔ `MAINTENANCE`) <br>• Create maintenance records (description, dates, and repair costs) |
| **Admin (`ROLE_ADMIN`)** | • Register new vehicles into the fleet (including purchase price, date, and mileage)<br>• View full vehicle inventory without status filters<br>• Inspect complete rental and maintenance histories per vehicle<br>• Soft-delete vehicles by updating status to `INACTIVE` |

**Statuses:**
- Car: `AVAILABLE`, `BOOKED`, `MAINTENANCE`, `INACTIVE`
- Booking: `PENDING`, `CONFIRMED`, `CANCELLED`


## Getting Started
 
**Requirements:** JDK 21+, Docker
 
```bash
git clone https://github.com/wzxcff/CarRental.git
cd CarRental
 
docker compose up -d      # PostgreSQL
./mvnw spring-boot:run    # Flyway applies migrations on startup
```
 
Run tests (Testcontainers starts its own database):
 
```bash
./mvnw clean verify
```

## API Documentation

When the application is running, the interactive Swagger UI is available at:
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI Spec:** `http://localhost:8080/v3/api-docs`
* **Health:** `http://localhost:8080/actuator/health`


## Project Architecture

The application adheres to the **Package by Feature** architectural pattern to ensure domain isolation, high cohesion, and scalability:

```text
com.rental.carflow
├── booking       # Rental bookings, pricing calculations, & date overlap checks
├── car           # Fleet management, metadata, & availability queries
├── location      # Station locations (airports, train stations)
├── maintenance   # Mechanic service records & repair history
├── user          # Security, authentication, roles (USER, MECHANIC, ADMIN)
└── config        # Global security configurations, OpenAPI, & application beans
```

## Design Decisions
 
- **Flyway instead of `ddl-auto`:** schema changes are explicit and reviewable.
- **Testcontainers instead of H2:** tests run on the same engine as production.
- **Soft delete (`INACTIVE`):** booking and maintenance history stays intact.
