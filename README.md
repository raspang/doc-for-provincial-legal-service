# 📜 Provincial Legal Services Document Tracker

**A document monitoring system for the Provincial Legal Services of Lanao del Sur**
Bangsamoro Autonomous Region in Muslim Mindanao (BARMM), Republic of the Philippines

> Developed by **ICTO – Lanao del Sur** · [facebook.com/icto.lanaodelsur](https://www.facebook.com/icto.lanaodelsur)

Built with [JHipster 9](https://www.jhipster.tech/) — Spring Boot (Java 21) + Angular + MariaDB

---

## 📑 Table of Contents

1. [About the Project](#-about-the-project)
2. [Key Features](#-key-features)
3. [How the Tracking Works](#-how-the-tracking-works-business-logic)
4. [Screenshots](#-screenshots)
5. [Tech Stack](#-tech-stack)
6. [System Architecture](#-system-architecture)
7. [Getting Started (For Developers)](#-getting-started-for-developers)
8. [Configuration Guide](#-configuration-guide)
9. [Deployment](#-deployment)
10. [Testing & Code Quality](#-testing--code-quality)
11. [Engineering Highlights (For Interviewers)](#-engineering-highlights-for-interviewers)
12. [Roadmap](#-roadmap)
13. [Credits](#-credits)

---

## 🎯 About the Project

### For Everyone (Plain English)

Government offices receive and release **hundreds of documents every month** — memoranda, letters, certificates, and legal requests. Tracking them manually using logbooks is slow, error-prone, and makes it hard to know which documents are **overdue**.

This system digitizes that process for the **Provincial Legal Services of Lanao del Sur**:

- 📥 **Incomings (Received Documents)** — Every document that comes in is logged with its type, the office it came from, who is responsible for it, and what action was requested.
- 📤 **Outgoings (Document References)** — Every document that goes out is assigned a unique reference number so it can always be traced.
- ⏰ **Deadline Tracking** — The system automatically computes each document's due date based on how complex the transaction is (Simple = 3 days, Complex = 7 days, Highly Technical = 14 days).
- 🔔 **Automatic Email Reminders** — Every morning at 8:00 AM, the system checks for overdue documents and emails each responsible person **individually** with their own list of pending items.
- 📊 **Live Dashboard** — Administrators see weekly incoming trends, staff workload distribution, and outgoing document statistics at a glance.
- 🖨️ **Official PDF Reports** — One-click export of reports with the official government letterhead.

### For Technical Readers

A full-stack monolith generated with **JHipster 9** (Spring Boot backend + Angular frontend), following a layered architecture: REST resources → service layer (with MapStruct DTOs) → Spring Data JPA repositories, with Liquibase-managed schema migrations, JWT authentication, Ehcache second-level caching, and a scheduled async notification engine. The Angular frontend uses standalone components, signals, and Chart.js for analytics.

---

## ✨ Key Features

| Feature                               | Description                                                                                                                                             |
| ------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 📥 **Received Document Management**   | Log incoming documents with type, office, requested action, responsible person, and transaction complexity                                              |
| 📤 **Document Reference Management**  | Track outgoing documents with unique reference numbers, authors, release dates, and routing                                                             |
| ⏱️ **Automatic Due Date Computation** | `Due Date = Date Received + Transaction Type Target Days`                                                                                               |
| 🚨 **Overdue Detection**              | Documents with a warning status are flagged when `Days Before Due ≤ 0` with a blinking highlight in the UI                                              |
| 📧 **Scheduled Email Notifications**  | Daily 8:00 AM cron job; documents are **grouped by responsible person's email** and sent individually via Gmail SMTP with a branded HTML email template |
| 📊 **Analytics Dashboard**            | Chart.js-powered graphs: monthly weekly trend (incomings), workload by responsible person (doughnut), outgoings by document type (bar)                  |
| 🖨️ **PDF Report Export**              | Filtered exports with official BARMM/Lanao del Sur letterhead using jsPDF                                                                               |
| 🔎 **Advanced Filtering & Sorting**   | Dynamic JPA criteria filtering by date range, type, status, person, and text search                                                                     |
| 🔐 **Role-Based Security**            | JWT authentication with role-based access control                                                                                                       |
| 🕵️ **Audit Trail**                    | Every record tracks `createdBy`, `createdDate`, `lastModifiedBy`, `lastModifiedDate` automatically                                                      |
| 📱 **Responsive UI**                  | Works on desktop and mobile browsers                                                                                                                    |

---

## ⚙️ How the Tracking Works (Business Logic)

### Transaction Types & Deadlines

Each incoming document is classified by complexity, which determines the processing deadline:

| Transaction Type    | Target Days | Example                    |
| ------------------- | ----------- | -------------------------- |
| 🟢 SIMPLE           | 3 days      | Routine requests           |
| 🟡 COMPLEX          | 7 days      | Multi-step reviews         |
| 🔴 HIGHLY TECHNICAL | 14 days     | Specialized legal opinions |
| ⚪ NOT APPLICABLE   | 0 days      | For reference only         |

### Due Date Formula
