# 🚕 Travel Company Cab Booking System
### Spring Boot Microservices | REST API | JUnit 5 | Thymeleaf | H2 Database

---

## 📖 Description

A full-stack **Travel Management System** built for George's cab booking company using **Spring Boot Microservices** and a **REST architecture**. The system handles cab bookings, fare calculations, and passenger profile management through independent microservices — all testable via a browser, Postman, or the built-in HTML front-end.

Developed as part of a Software Engineering assignment to demonstrate:
- Microservice design with Spring Boot
- RESTful Web Services
- JPA/Hibernate data persistence
- Thymeleaf HTML front-end
- JUnit 5 unit testing

---

## 🏗️ Architecture

```
com.travel.george/
│
├── model/
│   ├── CabBooking.java          → Booking entity (JPA)
│   └── Passenger.java           → Passenger profile entity (JPA)
│
├── repository/
│   ├── CabBookingRepository     → Spring Data JPA CRUD
│   └── PassengerRepository      → Spring Data JPA CRUD
│
├── service/  ← MICROSERVICES
│   ├── CabBookingService.java   → Microservice 1: Booking logic
│   ├── FareCalculatorService.java → Microservice 2: Fare calculation
│   └── PassengerService.java    → Passenger profile management
│
├── controller/
│   ├── CabBookingController.java  → REST + HTML endpoints
│   └── PassengerController.java   → REST + HTML endpoints
│
└── SpringBootAssignmentSolutionApplication.java → Main entry point
```

---

## ✨ Features

- ✅ **Book a Cab** — pick-up, drop-off, cab type, distance, passenger
- ✅ **Live Fare Preview** — auto-calculates fare as you type
- ✅ **View All Bookings** — table with stats (total, confirmed, cancelled, revenue)
- ✅ **Cancel / Delete Bookings** — via REST API
- ✅ **Passenger Profiles** — add, view, update, delete with membership tiers
- ✅ **Fare Calculator Microservice** — Economy / Premium / Luxury rates
- ✅ **H2 In-Memory Database** — no setup required
- ✅ **H2 Console** — live database browser at `/h2-console`
- ✅ **24 JUnit 5 Unit Tests** — all passing
- ✅ **Executable JAR** — run anywhere with Java 17+

---

## 💰 Fare Structure

| Type | Cab | Rate | Minimum Fare |
|------|-----|------|-------------|
| 1 | Economy | ₹10 / km | ₹50 |
| 2 | Premium | ₹15 / km | ₹50 |
| 3 | Luxury  | ₹25 / km | ₹50 |

---

## 🛠️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| Java 17 | Core language |
| Spring Boot 3.2 | Application framework |
| Spring Web (MVC) | REST controllers |
| Spring Data JPA | Database ORM |
| Hibernate | JPA implementation |
| H2 Database 2.1.214 | In-memory database |
| Thymeleaf | HTML template engine |
| JUnit 5 | Unit testing |
| Maven | Build & dependency management |

---

## 🚀 Getting Started

### Prerequisites
- Java 17 installed
- Maven 3.6+ installed (or use the included `mvnw.cmd`)

### Run the Application

**Option 1 — Using the JAR (recommended):**
```bash
java -jar target/SpringBootAssignmentSolution-0.0.1-SNAPSHOT.jar
```

**Option 2 — Using Maven:**
```bash
mvn spring-boot:run
```

**Option 3 — Using Maven Wrapper (Windows):**
```bash
mvnw.cmd spring-boot:run
```

The app starts at: **http://localhost:8080**

---

## 🌐 Front-End Pages

| Page | URL |
|------|-----|
| 🚕 Book a Cab | http://localhost:8080/cab/book-form |
| 📋 View All Bookings | http://localhost:8080/cab/view-bookings |
| 👤 Passenger Profiles | http://localhost:8080/passenger/profile-form |
| 🗄️ H2 Database Console | http://localhost:8080/h2-console |

> **H2 Console Login:** JDBC URL = `jdbc:h2:mem:traveldb` · Username = `sa` · Password = *(blank)*

---

## 📡 REST API Reference

### Cab Booking Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/cab/book` | Book a cab |
| `GET` | `/cab/bookings` | Get all bookings |
| `GET` | `/cab/booking/{id}` | Get booking by ID |
| `PUT` | `/cab/booking/{id}/cancel` | Cancel a booking |
| `DELETE` | `/cab/booking/{id}` | Delete a booking |
| `GET` | `/cab/fare?distance=10&type=2` | Calculate fare |
| `GET` | `/cab/count` | Total bookings count |

### Passenger Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/passenger/add` | Add a passenger |
| `GET` | `/passenger/all` | Get all passengers |
| `GET` | `/passenger/{id}` | Get passenger by ID |
| `PUT` | `/passenger/{id}` | Update passenger |
| `DELETE` | `/passenger/{id}` | Delete passenger |
| `GET` | `/passenger/count` | Total passenger count |

---

## 📬 Postman Examples

### Book a Cab
```http
POST http://localhost:8080/cab/book
Content-Type: application/json

{
  "from": "Home",
  "to": "Office",
  "typeOfCab": 2,
  "distanceKm": 12.0,
  "passengerName": "Alice"
}
```
**Response:**
```json
{
  "bookingId": 1,
  "from": "Home",
  "to": "Office",
  "typeOfCab": 2,
  "distanceKm": 12.0,
  "fare": 180.0,
  "passengerName": "Alice",
  "status": "CONFIRMED",
  "bookingTime": "2026-08-15T10:30:00"
}
```

### Calculate Fare
```http
GET http://localhost:8080/cab/fare?distance=15&type=3
```
**Response:**
```json
{
  "distanceKm": 15.0,
  "cabType": 3,
  "cabTypeName": "Luxury",
  "ratePerKm": 25.0,
  "calculatedFare": 375.0,
  "currency": "INR"
}
```

### Add a Passenger
```http
POST http://localhost:8080/passenger/add
Content-Type: application/json

{
  "name": "Alice Smith",
  "email": "alice@email.com",
  "phone": "9876543210",
  "address": "123 Main St, Mumbai",
  "membershipType": "GOLD"
}
```

---

## 🧪 Running Tests

```bash
mvn clean test
```

**Result:** `Tests run: 24, Failures: 0, Errors: 0, Skipped: 0` ✅

### Test Coverage

| Area | Tests |
|------|-------|
| Application context | 1 |
| Fare Calculator (all types, edge cases) | 7 |
| Cab Booking Service (CRUD) | 6 |
| Passenger Service (CRUD) | 5 |
| REST Controller responses | 5 |
| **Total** | **24** |

---

## 📦 Build Executable JAR

```bash
mvn clean package
```

Output: `target/SpringBootAssignmentSolution-0.0.1-SNAPSHOT.jar`

Run anywhere:
```bash
java -jar target/SpringBootAssignmentSolution-0.0.1-SNAPSHOT.jar
```

---

## 📁 Project Structure

```
travel company  microservice/
├── pom.xml
├── README.md
├── mvnw / mvnw.cmd
├── src/
│   ├── main/
│   │   ├── java/com/travel/george/
│   │   │   ├── SpringBootAssignmentSolutionApplication.java
│   │   │   ├── model/
│   │   │   │   ├── CabBooking.java
│   │   │   │   └── Passenger.java
│   │   │   ├── repository/
│   │   │   │   ├── CabBookingRepository.java
│   │   │   │   └── PassengerRepository.java
│   │   │   ├── service/
│   │   │   │   ├── CabBookingService.java
│   │   │   │   ├── FareCalculatorService.java
│   │   │   │   └── PassengerService.java
│   │   │   └── controller/
│   │   │       ├── CabBookingController.java
│   │   │       └── PassengerController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── book-cab.html
│   │           ├── view-bookings.html
│   │           └── passenger-profile.html
│   └── test/
│       ├── java/com/travel/george/
│       │   └── SpringBootAssignmentSolutionApplicationTests.java
│       └── resources/
│           └── application.properties
└── target/
    └── SpringBootAssignmentSolution-0.0.1-SNAPSHOT.jar
```

---

## 👨‍💻 Author

**Developed by:** Kia's Software Solution Company
**For:** George's Travel — Cab Booking Management System
**Framework:** Spring Boot 3.2 · Java 17 · Maven

---

## 📝 License

This project was developed as an academic assignment for demonstrating Spring Boot Microservices architecture.
