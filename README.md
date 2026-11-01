# CarRental — REST API Service

![CarRental CI](https://github.com/wzxcff/CarRental/actions/workflows/ci.yml/badge.svg)
![Java Version](https://img.shields.io/badge/Java-25-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)

CarRental is a backend RESTful API service for daily vehicle rentals across fixed station locations (e.g., airports, train stations). The system features Role-Based Access Control (RBAC) supporting Users, Mechanics, and Administrators, integrated date-range availability searching, and maintenance tracking.

## Tech Stack

* **Language:** Java 25
* **Framework:** Spring Boot (Spring Web, Spring Security, Spring Data JPA, Validation, Actuator)
* **Database & Migrations:** PostgreSQL, Flyway
* **Testing:** Testcontainers, JUnit 5, MockMvc
* **Documentation & Tooling:** OpenAPI 3 / Swagger UI (`springdoc-openapi`), Maven, Docker, GitHub Actions (CI/CD)
* **Architecture:** Package by Feature

## Key Features & Role Matrix

| Role | Key Capabilities |
| :--- | :--- |
| **User (`ROLE_USER`)** | • Account registration & JWT authentication<br>• Search available cars by date interval, car type, and station location<br>• Sort car search results by daily price<br>• Book vehicles with designated pickup and return locations<br>• View personal rental history |
| **Mechanic (`ROLE_MECHANIC`)** | • Search vehicle records by license plate<br>• Toggle vehicle status (`AVAILABLE` $\leftrightarrow$ `MAINTENANCE`) <br>• Create maintenance records (description, dates, and repair costs) |
| **Admin (`ROLE_ADMIN`)** | • Register new vehicles into the fleet (including purchase price, date, and mileage)<br>• View full vehicle inventory without status filters<br>• Inspect complete rental and maintenance histories per vehicle<br>• Soft-delete vehicles by updating status to `INACTIVE` |

## Getting Started

### Prerequisites

* JDK 25+
* Docker Engine / Docker Desktop (required for PostgreSQL Testcontainers during build & verification)

### Build & Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/wzxcff/CarRental.git
   cd CarRental
   ```

2. **Build and verify with integration tests:**
   ```bash
   ./mvnw clean verify
   ```

3. **Start the application locally:**
   ```bash
   ./mvnw spring-boot:run
   ```


## API Documentation

When the application is running, the interactive Swagger UI is available at:
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI Spec:** `http://localhost:8080/v3/api-docs`


## 📁 Project Architecture

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
