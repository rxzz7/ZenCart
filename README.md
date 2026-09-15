# ZenCart

ZenCart is a cloud-native e-commerce backend built as a set of independent **Spring Boot microservices**. Each service owns its own database, communicates with the others over **REST** and **Apache Kafka**, registers with a central **Eureka** service registry, and is reachable through a single **Spring Cloud Gateway** entry point. The whole system is containerized with **Docker Compose** for one-command local deployment.

## Architecture

| Service | Port | Responsibility | Database |
|---|---|---|---|
| `eureka-server-service` | 9090 | Service discovery / registry | — |
| `api-gateway` | 8087 | Single entry point, routes requests to downstream services | — |
| `auth-service` | 8081 | User registration/login, JWT issuance & refresh (RS256), email verification, password reset, address management | `zencart_user_db` |
| `product-service` | 8082 | Product catalog & categories | `zencart_product_db` |
| `order-service` | 8083 | Shopping cart & order lifecycle | `zencart_order_db` |
| `payment-service` | 8084 | Payment processing, publishes payment events to Kafka | `zencart_payment_db` |
| `notification-service` | 8085 | Consumes Kafka events, sends email notifications | `zencart_notification_db` |

```
Client
  │
  ▼
api-gateway (8087) ──registers with──► eureka-server (9090) ◄──registers with── all services
  │
  ├── auth-service (8081)      ── issues JWTs (RS256)
  ├── product-service (8082)   ── validates JWTs against auth-service JWKS
  ├── order-service (8083)     ── validates JWTs against auth-service JWKS
  └── payment-service (8084) ──Kafka──► notification-service (8085)
```

Each downstream service validates incoming requests as an **OAuth2 Resource Server**, trusting JWTs signed by `auth-service` and verified via its `/.well-known/jwks.json` endpoint — so authentication is centralized while authorization checks stay local to each service.

## Tech stack

- **Language / Framework:** Java 17, Spring Boot 4, Spring MVC
- **Microservices & Cloud:** Spring Cloud Gateway (routing), Netflix Eureka (service discovery/registry), Spring Cloud LoadBalancer
- **Security:** Spring Security, JWT (RS256 via `jjwt`), OAuth2 Resource Server, centralized auth with distributed authorization
- **Persistence:** Spring Data JPA, MySQL 8, Flyway (versioned DB migrations, one schema per service — Database-per-Service pattern)
- **Messaging:** Apache Kafka (event-driven, async communication between `payment-service` and `notification-service`)
- **Observability:** Spring Boot Actuator, Micrometer Tracing + Zipkin (distributed tracing)
- **API Documentation:** springdoc-openapi (Swagger UI) on every service
- **Testing:** JUnit 5, Spring Boot Test
- **Build / Tooling:** Maven, Lombok, ModelMapper
- **Containerization / DevOps:** Docker, Docker Compose (7-service multi-container orchestration)

## Key features

- Stateless authentication via short-lived JWTs signed with an RSA key pair, verified downstream through a JWKS endpoint — no shared secrets between services
- Independent, horizontally scalable services, each with its own MySQL schema and Flyway-managed migrations
- Asynchronous, decoupled payment → notification flow using Kafka producers/consumers instead of synchronous calls
- Centralized routing and dynamic service discovery via Spring Cloud Gateway + Eureka, so clients only ever talk to one address
- End-to-end request tracing across services with Micrometer + Zipkin
- Self-documenting REST APIs via Swagger/OpenAPI on every service
- Fully reproducible local environment: `docker compose up` starts MySQL, Kafka, Zookeeper, Zipkin, Eureka, the gateway, and all five business services

## Getting started

### Prerequisites

- Java 17+
- Maven (or the included `./mvnw` wrapper in each service)
- Docker & Docker Compose
- A Gmail account with an [app password](https://myaccount.google.com/apppasswords) (for `auth-service` / `notification-service` emails)

### Run everything with Docker Compose

```bash
cp .env.example .env
# edit .env and fill in MAIL_USERNAME / MAIL_PASSWORD

docker compose up -d --build
```

This starts MySQL, Kafka + Zookeeper, Zipkin, Eureka, the API Gateway, and all business services. Once up:

- API Gateway: `http://localhost:8087`
- Eureka Dashboard: `http://localhost:9090`
- Auth API docs: `http://localhost:8081/swagger-ui.html`
- Product API docs: `http://localhost:8082/swagger-ui.html`
- Order API docs: `http://localhost:8083/swagger-ui.html`
- Payment API docs: `http://localhost:8084/swagger-ui.html`
- Zipkin UI: `http://localhost:9411`

Stop and remove containers (keeping the MySQL volume):

```bash
docker compose down
```

Stop and wipe all data:

```bash
docker compose down -v
```

### Run a single service locally (without Docker)

Each service is a standalone Maven project. Example for `auth-service`:

```bash
cd auth-service
./mvnw spring-boot:run
```

You'll need MySQL running locally on `3306` (user `root` / password `password`, or update `application.yml`), and, for `auth-service`, an RSA key pair at `src/main/resources/keys/private_key.pem` and `public_key.pem` for signing JWTs:

```bash
openssl genrsa -out private_key.pem 2048
openssl rsa -in private_key.pem -pubout -out public_key.pem
```

## Project structure

```
ZenCart/
├── eureka-server-service/   # Service discovery/registry
├── api-gateway/             # Single entry point, request routing
├── auth-service/            # Authentication, users, addresses, JWT issuance
├── product-service/         # Product catalog
├── order-service/           # Cart & orders
├── payment-service/         # Payments, Kafka producer
├── notification-service/    # Kafka consumer, email notifications
├── docker-compose.yml       # MySQL, Kafka, Zookeeper, Zipkin, Eureka, Gateway + all services
├── mysql/init-db.sql        # Creates one database per service
└── .env.example             # Template for local secrets
```

## Known limitations

- **Kafka topic/consumer configuration** is minimal and intended for local development, not production tuning.
- **JWT keys are not included** in the repo for security reasons — each developer generates their own local key pair.
- No CI/CD pipeline or centralized config server (Spring Cloud Config) yet.