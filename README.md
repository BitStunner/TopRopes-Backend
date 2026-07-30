# TopRopes Backend

Java + Spring Boot + Gradle + PostgreSQL backend for TopRopes.

## Requirements

- Java 17 (OpenJDK 17.0.19)
- Docker + Docker Compose

## Run PostgreSQL

```bash
docker compose up -d
```

## Run API

```bash
./gradlew bootRun
```

## Default local config

- DB URL: `jdbc:postgresql://localhost:5432/topropes`
- DB user: `topropes`
- DB password: `topropes`
- API base URL: `http://localhost:8080/api/v1`

## API Contract

OpenAPI document: `docs/openapi.yaml`
