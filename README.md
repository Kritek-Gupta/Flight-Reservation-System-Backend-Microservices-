# Flight-Reservation-System-Backend-Microservices-
Flight Reservation System (FRS) – Backend Microservices ✈️
📌 Overview

Flight Reservation System (FRS) is a microservices-based airline reservation platform built with Spring Boot and Spring Cloud, designed to handle core airline operations such as user authentication, flight search, passenger management, booking lifecycle, and payments.

The project emphasizes scalability, resilience, and clean architecture, making it suitable for production-grade airline reservation systems.

🚀 Features

🔐 User Authentication & Authorization

✈️ Flight Search – search by route, date, and availability

👥 Passenger Management – manage passenger details seamlessly

📑 Booking Lifecycle – create, view, and cancel reservations

💳 Payment Integration – supports dynamic discounts and secure transactions

⚡ Resilience & Fault Tolerance – circuit breaker patterns & load balancing

🛠 Centralized Exception Handling for consistent error responses

📖 Interactive API Documentation using Swagger

🏗️ Architecture

The platform is based on microservices architecture for modularity and scalability.

Tech Stack

Backend Framework: Spring Boot

API Gateway: Spring Cloud Gateway

Service Discovery: Consul

Resilience: Circuit Breaker (Resilience4j) & Load Balancing

Database Layer: JPA + Repository Pattern

DTO–Entity Mapping: ModelMapper

Boilerplate Reduction: Lombok

Testing: JUnit & Mockito (80%+ coverage)

Documentation: Swagger

Layered Design

Controller Layer – Handles REST APIs

Service Layer – Business logic

Repository Layer – Database interactions

📂 Project Structure
FRS/
 ├── api-gateway/              # Spring Cloud Gateway
 ├── auth-service/             # User authentication & authorization
 ├── flight-service/           # Flight search & management
 ├── booking-service/          # Reservation lifecycle
 ├── passenger-service/        # Passenger information management
 ├── payment-service/          # Payment integration
 ├── common/                   # Shared DTOs & utilities
 └── docs/                     # Swagger, diagrams, API reference

⚙️ Setup & Installation
Prerequisites

Java 17+

Maven 3.8+

Docker (for containerized services & Consul)

Steps

Clone the repository:

Start Consul for service discovery:

docker run -d --name=consul -p 8500:8500 consul


Build and run services:

mvn clean install
mvn spring-boot:run -pl api-gateway


Access Swagger UI:

http://localhost:8080/swagger-ui.html

🧪 Testing

Unit & integration tests with JUnit & Mockito

Achieved 80%+ coverage across all services

Run tests:

mvn test

📊 Future Enhancements

✨ Add seat selection & upgrade options

✨ Integrate third-party payment gateways

✨ Implement notification service (SMS/Email)

✨ Deploy on Kubernetes with CI/CD pipelines

🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you’d like to change.
