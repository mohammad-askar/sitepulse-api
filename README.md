# SitePulse API

SitePulse is a backend application for managing facility service operations across multiple client sites.

The platform will allow service companies to manage clients, sites, contracts, recurring tasks, worker assignments, work evidence, service issues, and performance reports.

## Current Status

The project is under active development.

## Technology Stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Docker
- JUnit
- Testcontainers

## Local Development
## Local Database

Copy the example environment file:

```bash
cp .env.example .env
Run the tests:

```bash
./mvnw test
## Local Database

Copy the example environment file:

```bash
cp .env.example .env
## Testing

The project includes unit, web-layer, and PostgreSQL integration tests.

Docker must be running for integration tests because PostgreSQL is started automatically using Testcontainers.

Run all tests:

```bash
./mvnw clean test