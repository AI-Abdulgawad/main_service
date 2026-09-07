# Main Service

The **public-facing entry point** of a small Spring Cloud microservices system. It serves a landing page to end users and, behind the scenes, discovers and calls `item-service` through a **Feign client** and Eureka — without ever hardcoding that service's URL.

## Overview

This service is the "front door" of the application: it's what a browser talks to. Rather than owning any data itself, it delegates to `item-service` for the actual `Dog` records, using a declarative Feign client resolved dynamically via the Eureka service registry. This demonstrates the classic microservices pattern of a thin, user-facing aggregator sitting in front of one or more backend data services.

## Role in the System

This is one of three microservices that make up a single application:

| Service | Role | Port |
|---|---|---|
| [eureka-server](https://github.com/AI-Abdulgawad/eureka-server) | Service discovery registry | `8761` |
| [item-service](https://github.com/AI-Abdulgawad/item_service) | Owns and serves dog data (JPA + H2) | `8080` |
| **main-service** *(this repo)* | Public-facing aggregator, calls item-service via Eureka/Feign | `8081` |

```
                ┌────────────────────┐
                │   Eureka Server     │  (port 8761)
                └─────────▲──────────┘
                          │ register / discover
              ┌───────────┴────────────┐
              │                        │
     ┌────────┴────────┐     ┌─────────┴─────────┐
     │  item-service    │     │   main-service     │
     │  port 8080       │◄────┤   (this repo)      │
     │  owns Dog data   │Feign│   port 8081         │
     └──────────────────┘     └────────────────────┘
                                        ▲
                                        │
                                   Browser / client
```

**Start [eureka-server](https://github.com/AI-Abdulgawad/eureka-server) first, then [item-service](https://github.com/AI-Abdulgawad/item_service), then this service** — `main-service` needs both to be registered and reachable to serve dog data successfully.

## Features

- **Landing page** served as a static HTML page (`/`)
- **Feign client** (`DogService`) that declaratively calls `item-service`'s `/getAllDogs` endpoint by logical service name — no hardcoded host or port
- **Service discovery via Eureka** — resolves `item-service`'s actual network location at call time
- **`/getAllDogs` REST endpoint** exposed to the browser/client, which internally proxies to `item-service`
- **springdoc-openapi/Swagger** included for interactive API documentation

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.3, Spring Cloud 2025.0.0 |
| Discovery | Netflix Eureka Client |
| Inter-service calls | Spring Cloud OpenFeign |
| API Docs | springdoc-openapi (Swagger UI) |
| Tooling | Lombok |
| Build | Maven (with Maven Wrapper) |

## Architecture

```
Browser  ──GET /getAllDogs──►  MainController
                                     │
                                     ▼
                          DogService (Feign client, @FeignClient(name = "item-service"))
                                     │
                          resolves "item-service" via Eureka
                                     ▼
                          item-service: GET /getAllDogs
                                     │
                                     ▼
                          Dog[] returned back to the browser
```

## Getting Started

### Prerequisites
- Java 17+
- [eureka-server](https://github.com/AI-Abdulgawad/eureka-server) running on `localhost:8761`
- [item-service](https://github.com/AI-Abdulgawad/item_service) running and registered as `item-service`
- No local Maven install required (Maven Wrapper included)

### Run locally

```bash
git clone https://github.com/AI-Abdulgawad/main_service.git
cd main_service
./mvnw spring-boot:run
```

The app starts on `http://localhost:8081`.

- **Landing page**: `http://localhost:8081/`
- **Get all dogs (via item-service)**: `GET http://localhost:8081/getAllDogs`
- **Swagger UI**: `http://localhost:8081/swagger-ui/index.html`

## Project Structure

```
src/main/java/com/udacity/main_service/
├── MainServiceApplication.java     # @EnableFeignClients entry point
├── controller/
│   ├── MainController.java         # GET /getAllDogs — delegates to item-service
│   └── RenderController.java       # (reserved for future page routing)
├── service/
│   └── DogService.java             # @FeignClient(name = "item-service") interface
└── model/
    └── Dog.java                    # Plain DTO mirroring item-service's Dog shape

src/main/resources/
├── static/index.html               # Landing page
└── application.properties
```

## What This Project Demonstrates

- Building a **declarative Feign client** that calls another service by logical name, resolved through Eureka, rather than a hardcoded URL
- The **aggregator/gateway-lite** pattern common in microservices: a user-facing service that composes data from backend services
- Registering with a discovery server as a Eureka client (`spring-cloud-starter-netflix-eureka-client`)
- Serving both a static frontend and a JSON API from the same Spring Boot app

## Related Services

- [eureka-server](https://github.com/AI-Abdulgawad/eureka-server) — the discovery registry this service and item-service both register with
- [item-service](https://github.com/AI-Abdulgawad/item_service) — the backend service this app calls via Feign to retrieve dog data

## Possible Extensions

- Add error handling/fallbacks (e.g. Resilience4j) for when `item-service` is unavailable
- Render the dog list directly in the landing page rather than only exposing it as JSON
- Add an API gateway (e.g. Spring Cloud Gateway) in front of both services for routing and cross-cutting concerns

---

*This is a demo project (originally built as part of a Udacity course) intended to showcase Spring Cloud microservice fundamentals.*
