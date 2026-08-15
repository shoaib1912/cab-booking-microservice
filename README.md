# cab-booking-microservice
travel Management System built for George’s company using Spring Boot Microservices and REST APIs. It manages cab bookings, fare calculations, and passenger profiles through independent services. Uses JPA/Hibernate for persistence, Thymeleaf for the web interface, and JUnit 5 for testing, demonstrating scalable and maintainable backend development.
# Travel Management System

A cab booking management system built with **Spring Boot 3.2, Java 17, and Maven**.

## Features
- Cab booking and fare calculation
- Passenger profile management
- REST APIs
- JPA/Hibernate database persistence
- Thymeleaf web interface
- JUnit 5 testing

## Project Structure

```text
src/main/java/com/travel/george/
├── model/
├── repository/
├── service/
└── controller/

src/main/resources/
├── application.properties
└── templates/

src/test/
└── java/com/travel/george/
