# 📜 Provincial Legal Services Document Tracker

**A document monitoring & deadline-tracking system for the Provincial Legal Services of Lanao del Sur**
Bangsamoro Autonomous Region in Muslim Mindanao (BARMM) · Republic of the Philippines

> Developed by **ICTO – Lanao del Sur** · [facebook.com/icto.lanaodelsur](https://www.facebook.com/icto.lanaodelsur)
> Built with [JHipster 9](https://www.jhipster.tech/) — Spring Boot + Angular + MariaDB

---

## 📑 Table of Contents

1. [About the Project](#-about-the-project)
2. [Key Features](#-key-features)
3. [How the Tracking Works](#-how-the-tracking-works)
4. [Screenshots](#-screenshots)
5. [Tech Stack](#-tech-stack)
6. [System Architecture](#-system-architecture)
7. [Domain Model](#-domain-model)
8. [Getting Started (For Developers)](#-getting-started-for-developers)
9. [Configuration Guide](#-configuration-guide)
10. [Deployment](#-deployment)
11. [Testing & Code Quality](#-testing--code-quality)
12. [Engineering Highlights (For Interviewers)](#-engineering-highlights-for-interviewers)
13. [Roadmap](#-roadmap)
14. [Credits & References](#-credits--references)

---

## 🎯 About the Project

### For Everyone (Plain English)

Government offices receive and release **hundreds of documents every month** — memoranda, letters, certificates, and legal requests. Tracking them manually using logbooks is slow, error-prone, and makes it nearly impossible to know which documents are **overdue**.

This system digitizes that entire process for the **Provincial Legal Services of Lanao del Sur**:

| What it does                            | Why it matters                                                                                                           |
| --------------------------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| 📥 **Logs incoming documents**          | Every document that comes in is recorded with its type, source office, responsible person, and requested action          |
| 📤 **Tracks outgoing documents**        | Every document that goes out gets a unique reference number so it can always be traced                                   |
| ⏰ **Computes deadlines automatically** | Due dates are calculated based on transaction complexity (Simple = 3 days, Complex = 7 days, Highly Technical = 14 days) |
| 🔔 **Sends email reminders**            | Every morning at 8:00 AM, overdue documents are emailed individually to each responsible person                          |
| 📊 **Shows live analytics**             | Administrators see weekly trends, staff workload, and outgoing statistics at a glance                                    |
| 🖨️ **Exports official PDF reports**     | One-click export with the official government letterhead                                                                 |
| 🕵️ **Records every change**             | Automatically tracks who created/updated each record and when                                                            |

### For Technical Readers

A full-stack monolith generated with **JHipster 9** (Spring Boot 4 backend + Angular standalone frontend), following a layered architecture: REST resources → service layer (MapStruct DTOs) → Spring Data JPA repositories, with Liquibase-managed schema migrations, JWT authentication, Ehcache second-level caching, and a scheduled async notification engine. The Angular frontend uses standalone components, signals, `ChangeDetectionStrategy.OnPush`, and Chart.js for analytics.

---

## ✨ Key Features

### 📥 Incomings (Received Documents)

- Log incoming documents with type, office, requested action, responsible person, and transaction complexity
- **Automatic due-date computation** based on transaction type
- **Overdue detection** with blinking UI highlight for documents past their deadline
- **Color-coded status badges** with dynamic text color (auto black/white based on background brightness)
- Advanced filtering by date range, document type, status, responsible person, and text search
- Sortable, paginated data table

### 📤 Outgoings (Document References)

- Track outgoing documents with unique reference numbers
- Record author, release date, and routing information
- Filter and sort by reference number, title, author, type, and date range

### 🔔 Automated Email Notifications

- **Daily cron job** (8:00 AM server time) scans for overdue documents
- Documents are **grouped by responsible person's email** — each person receives exactly one email containing only their documents
- Branded **HTML email template** with official government letterhead
- Sent via **Gmail SMTP** with App Password authentication

### 📊 Analytics Dashboard

- **Incomings Weekly Trend** (Bar Chart) — documents received per week for the current month
- **Workload by Responsible Person** (Doughnut Chart) — document distribution among staff for the current year
- **Outgoings by Document Type** (Bar Chart) — outgoing document counts by category for the current year

### 🖨️ PDF Report Export

- Client-side PDF generation using **jsPDF + autotable**
- Official government letterhead (Republic of the Philippines / BARMM / Province of Lanao del Sur)
- Landscape A4 format with alternating row colors
- Respects all active filters — export exactly what you see
- Page numbering footer

### 🕵️ Audit Trail

- Every entity automatically tracks `createdBy`, `createdDate`, `lastModifiedBy`, `lastModifiedDate`
- Populated from the JWT security context — no manual input needed
- Uses Spring Data JPA Auditing via `AbstractAuditingEntity`

### 🔐 Security

- JWT-based authentication with role-based access control
- Token validity: 24 hours (30 days with "Remember Me")
- 256-bit minimum secret key enforcement

---

## ⚙️ How the Tracking Works

### Transaction Types & Deadlines

Each incoming document is classified by complexity, which determines the processing deadline:

| Transaction Type    | Target Days | Example                    |
| ------------------- | ----------- | -------------------------- |
| 🟢 SIMPLE           | 3 days      | Routine requests           |
| 🟡 COMPLEX          | 7 days      | Multi-step reviews         |
| 🔴 HIGHLY TECHNICAL | 14 days     | Specialized legal opinions |
| ⚪ NOT APPLICABLE   | 0 days      | For reference only         |

### Due Date Formula

dueDate = dateReceived + transactionType.targetDays
daysBeforeDue = dueDate − today

### Overdue Detection

A document is **flagged for notification** when:

documentStatus.warning == true AND daysBeforeDue ≤ 0

In the UI, overdue documents with a warning status display a **blinking highlight animation** to draw immediate attention.

### Notification Flow

┌──────────────────────────────────────────────────────────────┐
│ Daily Cron (10:00 AM) │
│ └─► Query documents where documentStatus.warning = true │
│ └─► Compute dueDate & daysBeforeDue per document │
│ └─► Filter: daysBeforeDue ≤ 0 │
│ └─► Group by responsiblePerson.email │
│ └─► Send ONE email per person with THEIR documents only │
└──────────────────────────────────────────────────────────────┘

---

## 🖼️ Screenshots

> _TODO: Add screenshots before sharing publicly_

| Dashboard          | Received Documents | Email Notification | PDF Export         |
| ------------------ | ------------------ | ------------------ | ------------------ |
| _(add screenshot)_ | _(add screenshot)_ | _(add screenshot)_ | _(add screenshot)_ |

---

## 🧱 Tech Stack

### Backend

| Technology                         | Purpose                        |
| ---------------------------------- | ------------------------------ |
| **Java 21**                        | Runtime                        |
| **Spring Boot 4** (via JHipster 9) | Application framework          |
| **Spring Data JPA + Hibernate**    | ORM & data access              |
| **Ehcache**                        | Second-level entity caching    |
| **HikariCP**                       | Connection pooling             |
| **MariaDB** (prod) / **H2** (dev)  | Databases                      |
| **Liquibase**                      | Versioned schema migrations    |
| **Spring Security + JWT**          | Authentication & authorization |
| **Spring Scheduling**              | Cron-based notification engine |
| **Spring Mail + Thymeleaf**        | Branded HTML email templates   |
| **MapStruct**                      | Type-safe entity ⇄ DTO mapping |
| **Springdoc OpenAPI**              | API documentation              |

### Frontend

| Technology                                    | Purpose                    |
| --------------------------------------------- | -------------------------- |
| **Angular** (standalone components + signals) | UI framework               |
| **ChangeDetectionStrategy.OnPush**            | Performance optimization   |
| **Bootstrap 5 + ng-bootstrap**                | UI components              |
| **Chart.js** (pure, no wrapper)               | Dashboard analytics        |
| **jsPDF + autotable**                         | Client-side PDF generation |
| **dayjs**                                     | Date arithmetic            |
| **Font Awesome**                              | Icons                      |

### DevOps & Tooling

| Technology                                    | Purpose            |
| --------------------------------------------- | ------------------ |
| **Docker & Docker Compose**                   | MariaDB container  |
| **Maven + npm**                               | Dual build system  |
| **ESLint / Prettier / Checkstyle / Spotless** | Code quality       |
| **SonarQube**                                 | Static analysis    |
| **Jacoco**                                    | Code coverage      |
| **ArchUnit**                                  | Architecture tests |

---

## 🏛️ System Architecture

┌─────────────────────────────────────────────────────────────────┐
│ Angular SPA │
│ Dashboard (Chart.js) │ Document Lists │ PDF Export (jsPDF) │
│ Signals + OnPush │ Filters │ Letterhead Rendering │
└───────────────────────────────┬─────────────────────────────────┘
│ REST + JWT
┌───────────────────────────────▼─────────────────────────────────┐
│ Spring Boot Backend │
│ ┌────────────┐ ┌─────────────┐ ┌──────────────────────────┐ │
│ │ REST │ │ Service │ │ Scheduler (8AM cron) │ │
│ │ Resources │→ │ Layer │← │ → Email Fan-out │ │
│ └────────────┘ └──────┬──────┘ └──────────────────────────┘ │
│ │ MapStruct DTOs │
│ ┌──────────────────────▼──────────────────────────────────┐ │
│ │ Spring Data JPA Repositories (+ JPA Auditing) │ │
│ └──────────────────────┬──────────────────────────────────┘ │
└─────────────────────────┼───────────────────────────────────────┘
│ JDBC
┌───────────▼───────────┐ ┌──────────────────┐
│ MariaDB (Docker) │ │ Gmail SMTP │
│ Liquibase migrations │ │ (App Password) │
└───────────────────────┘ └──────────────────┘

---

## 📐 Domain Model

ReceivedDocument (Incoming)
├──► TypeOfDocument (required)
├──► DocumentStatus (required, has warning flag)
├──► ResponsiblePerson (email for notifications)
├──► RequestedAction
├──► Office
└──► TransactionType (targetDays for due-date calc)
DocumentReference (Outgoing)
└──► TypeOfDocument (required)
Supporting Entities:
ResponsiblePerson (name, position, email, contactNo)
RequestedAction (name)
TypeOfDocument (name)
Office (name, shortName)
DocumentStatus (name, color, warning)
TransactionType (name, targetDays, description)
All entities extend AbstractAuditingEntity:
createdBy / createdDate / lastModifiedBy / lastModifiedDate

---

## 🚀 Getting Started (For Developers)

### Prerequisites

| Tool       | Version | Notes                                               |
| ---------- | ------- | --------------------------------------------------- |
| Java (JDK) | 21+     | Required for the backend                            |
| Node.js    | v24+    | Required for the frontend (or let Maven install it) |
| Docker     | Latest  | For MariaDB (production mode)                       |
| Git        | Latest  | To clone the repo                                   |

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/legal.git
cd legal


Development mode uses an embedded H2 database (no setup needed).
Open two terminals:

# Terminal 1 — Backend (Spring Boot, starts on http://localhost:8080)
./mvnw

# Terminal 2 — Frontend (Angular dev server with hot reload)
./npmw install --legacy-peer-deps
./npmw run start






Run with MariaDB (Production-like)

docker compose -f src/main/docker/mariadb.yml up -d

./mvnw -Pprod


./mvnw -Pprod clean verify -DskipTests
java -jar target/legal-*.jar --spring.profiles.active=prod





spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: ${MAIL_PASSWORD}   # Gmail App Password (16 characters)
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          ssl:
            trust: smtp.gmail.com
        debug: false
jhipster:
  mail:
    from: your-email@gmail.com
    base-url: http://localhost:8080
```
