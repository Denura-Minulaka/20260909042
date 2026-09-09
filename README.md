# Government Training Management System (GTMS) - Backend API

> **Ministry of Finance Intern Selection - Practical Examination**  
> **Date**: September 9, 2026  
> **Technology Stack**: Java 17, Spring Boot 4, Spring Data JPA, Spring Security (BCrypt + JWT), MySQL, Swagger UI / OpenAPI 3

---

## 📌 Executive Summary & Solution for Task 1

The **Government Training Management System (GTMS)** is an enterprise Spring Boot RESTful web application built to digitize training management operations across 20+ government departments and 2,500+ officers.

### Solution for Task 1: Duplicate Nomination Prevention
In the manual process, officers were frequently nominated by multiple departments for the same training programme, requiring manual Excel merging by the coordinator. 

GTMS solves **Task 1** through a **3-Tier Defense Strategy**:
1. **Database Tier**: `@Table(uniqueConstraints = @UniqueConstraint(name = "uk_training_officer", columnNames = {"training_programme_id", "officer_id"}))` enforces complete data integrity at the MySQL database engine level.
2. **Service Tier**: `NominationServiceImpl` checks `findByTrainingProgrammeIdAndOfficerId()` before saving. If present, it throws a `DuplicateNominationException` detailing which officer, NIC, and department performed the prior nomination.
3. **API Presentation Tier**: `GlobalExceptionHandler` converts the exception into a clean `HTTP 409 CONFLICT` response with a structured JSON error body.

### Solution for Task 2: Limited Capacity & Waiting List Management
Training programmes have a fixed capacity (`maxParticipants`). When nominations exceed capacity:
1. **First-Come, First-Served (FCFS)**: The first `maxParticipants` valid nominations in order of submission timestamp (`nominatedAt`) are set to status **`CONFIRMED`**.
2. **Automated Waiting List**: Nominations beyond capacity are placed on **`WAITING_LIST`**.
3. **Automated Promotion on Cancellation**: Calling `PUT /api/v1/nominations/{id}/cancel` updates a confirmed nomination's status to `CANCELLED` and **automatically promotes the earliest waiting list officer** to `CONFIRMED`!

---

## 🛠️ Environment Configuration & Database Setup

* **Server Port**: `8082`
* **Base URL**: `http://localhost:8082/api/v1`
* **MySQL Database**: `gtmanagement`
* **MySQL Host / Port**: `localhost:4306` (Pre-configured for XAMPP MySQL on port 4306)
* **phpMyAdmin Access**: `http://localhost/phpmyadmin` (Inspects `gtmanagement` database)
* **Auto DB Initialization**: `spring.jpa.hibernate.ddl-auto=update` and `DataInitializer` bean automatically create tables and seed default users on application boot.

---

## 🔑 Initial Default Credentials (Data Seeder)

The application automatically seeds the database on startup with the following test credentials:

| Role | Email | Password | NIC | Department |
| :--- | :--- | :--- | :--- | :--- |
| **ROLE_COORDINATOR** | `coordinator@treasury.gov.lk` | `admin123` | `199011111111` | ITMD |
| **ROLE_DEPT_HEAD** | `depthead.fin@treasury.gov.lk` | `head123` | `199122222222` | Finance Division |
| **ROLE_DEPT_HEAD** | `depthead.adm@treasury.gov.lk` | `head123` | `199233333333` | Administration Division |
| **ROLE_OFFICER** | `perera@treasury.gov.lk` | `officer123` | `199512345678` | Finance Division |
| **ROLE_OFFICER** | `silva@treasury.gov.lk` | `officer123` | `199687654321` | Administration Division |

---

## 📖 Interactive API Documentation (Swagger UI)

Open your browser and navigate to:
👉 **[http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)**

### How to Authenticate in Swagger UI:
1. Call `POST /api/v1/auth/login` with `coordinator@treasury.gov.lk` / `admin123`.
2. Copy the returned JWT token string from the JSON response.
3. Click the green **Authorize 🔓** button at the top right of the Swagger page.
4. Paste the token into the **Value** box and click **Authorize**.

---

## 🌐 API Endpoint Matrix

### 1. Authentication (`/api/v1/auth`)
* `POST /api/v1/auth/login` - User login (Returns Bearer JWT Token)
* `POST /api/v1/auth/register` - User registration

### 2. Training Programmes (`/api/v1/trainings`)
* `POST /api/v1/trainings` - Create training programme (`ROLE_COORDINATOR` only)
* `GET /api/v1/trainings` - List all training programmes
* `GET /api/v1/trainings/{id}` - Get training details + current nomination count

### 3. Nominations (`/api/v1`)
* `POST /api/v1/trainings/{id}/nominations` - Submit officer nomination (**Task 1: Duplicate Prevention** & **Task 2: FCFS Status / Waiting List**)
* `GET /api/v1/trainings/{id}/nominations` - View combined nominations across departments (Replaces Excel merging)
* `GET /api/v1/trainings/{id}/waiting-list` - View ordered waiting list (**Task 2**)
* `PUT /api/v1/nominations/{id}/cancel` - Cancel nomination & **auto-promote earliest waiting-list officer** (**Task 2**)
* `PUT /api/v1/nominations/{id}/status` - Approve / Reject nomination (`ROLE_COORDINATOR` only)

---

## 🧪 Running Unit Tests

Run all unit tests (including Task 1 duplicate nomination tests) using Maven wrapper:

```bash
./mvnw clean test
```

---

## 📂 Clean Architecture Folder Structure

```text
c:/GitHub/20260909042/
├── src/
│   ├── main/
│   │   ├── java/com/example/_2/
│   │   │   ├── config/          # SecurityConfig, OpenApiConfig
│   │   │   ├── controller/      # AuthController, TrainingController, NominationController
│   │   │   ├── dto/             # LoginRequest, CreateNominationRequest, ApiResponse, etc.
│   │   │   ├── exception/       # ResourceNotFoundException, DuplicateNominationException, GlobalExceptionHandler
│   │   │   ├── model/          # User, Department, Venue, Trainer, TrainingProgramme, Nomination
│   │   │   ├── repository/     # UserRepository, NominationRepository, etc.
│   │   │   ├── security/       # JwtTokenProvider, JwtAuthenticationFilter, UserPrincipal
│   │   │   ├── service/        # AuthService, TrainingService, NominationService & Implementations
│   │   │   └── util/            # DataInitializer (CommandLineRunner)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/_2/
│           └── service/NominationServiceTest.java
├── pom.xml
└── README.md
```
