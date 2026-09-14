# ZenCart

ZenCart is an e-commerce backend built as a set of independent **Spring Boot microservices**. Each service owns its own database, communicates with the others over REST and Kafka, and can be built, deployed, and scaled on its own.

## Architecture

| Service | Port | Responsibility | Database |
|---|---|---|---|
| `auth-service` | 8081 | User registration/login, JWT issuance & refresh, email verification, password reset, address management | `zencart_user_db` |
| `product-service` | 8082 | Product catalog & categories | `zencart_product_db` |
| `order-service` | 8083 | Shopping cart & order lifecycle | `zencart_order_db` |
| `payment-service` | 8084 | Payment processing, publishes payment events to Kafka | `zencart_payment_db` |
| `notification-service` | 8085 | Consumes Kafka events and sends email notifications | `zencart_notification_db` |

```
Client
  │
  ├── auth-service (8081)      ── issues JWTs (RS256)
  ├── product-service (8082)   ── validates JWTs against auth-service JWKS
  ├── order-service (8083)     ── validates JWTs against auth-service JWKS
  └── payment-service (8084) ──Kafka──> notification-service (8085)
```

Each service validates incoming requests as an **OAuth2 Resource Server**, trusting JWTs signed by `auth-service` and verified via its `/.well-known/jwks.json` endpoint - so authentication is centralized but authorization checks stay local to each service.

## Tech stack

- **Language / Framework:** Java 17, Spring Boot 4, Spring MVC
- **Security:** Spring Security, JWT (RS256, via `jjwt`), OAuth2 Resource Server
- **Persistence:** Spring Data JPA, MySQL 8, Flyway (versioned DB migrations)
- **Messaging:** Apache Kafka (async events between `payment-service` and `notification-service`)
- **Service discovery:** Netflix Eureka client (see [Known limitations](#known-limitations))
- **Observability:** Spring Boot Actuator, Micrometer Tracing + Zipkin
- **API docs:** springdoc-openapi (Swagger UI)
- **Build/tooling:** Maven, Lombok, ModelMapper
- **Containerization:** Docker, Docker Compose

## Getting started

### Prerequisites

- Java 17+
- Maven (or use the included `./mvnw` wrapper in each service)
- Docker & Docker Compose
- A Gmail account with an [app password](https://myaccount.google.com/apppasswords) (for `auth-service` / `notification-service` emails)

### Run everything with Docker Compose

```bash
cp .env.example .env
# edit .env and fill in MAIL_USERNAME / MAIL_PASSWORD

docker compose up -d --build
```

This starts MySQL, Kafka + Zookeeper, Zipkin, and all five services. Once up:

- Auth API: `http://localhost:8081/swagger-ui.html`
- Product API: `http://localhost:8082/swagger-ui.html`
- Order API: `http://localhost:8083/swagger-ui.html`
- Payment API: `http://localhost:8084/swagger-ui.html`
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

You'll need MySQL running locally on `3306` (user `root` / password `password`, or update `application.yml`), and, for `auth-service`, an RSA key pair at `src/main/resources/keys/private_key.pem` and `public_key.pem` for signing JWTs (see [Known limitations](#known-limitations)).

## Known limitations

This project is a work in progress. A few gaps to be aware of before running it or discussing it in an interview:

- **No Eureka server yet.** `auth-service` and `product-service` are configured as Eureka clients (`@EnableDiscoveryClient`), but no `spring-cloud-starter-netflix-eureka-server` module exists in this repo. Services will log registration warnings until a discovery server is added.
- **No API Gateway.** Clients currently call each service directly on its own port; there's no single entry point yet.
- **JWT keys are not included.** `auth-service` expects `src/main/resources/keys/private_key.pem` and `public_key.pem` (RS256). Generate your own pair locally, e.g.:
  ```bash
  openssl genrsa -out private_key.pem 2048
  openssl rsa -in private_key.pem -pubout -out public_key.pem
  ```
- **Kafka topic/consumer configuration** is minimal and intended for local development, not production tuning.

## Project structure

```
ZenCart/
├── auth-service/           # Authentication, users, addresses, JWT issuance
├── product-service/        # Product catalog
├── order-service/          # Cart & orders
├── payment-service/        # Payments, Kafka producer
├── notification-service/   # Kafka consumer, email notifications
├── docker-compose.yml      # MySQL, Kafka, Zookeeper, Zipkin + all services
├── mysql/init-db.sql       # Creates one database per service
└── .env.example            # Template for local secrets
```
