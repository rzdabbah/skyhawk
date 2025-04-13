# NBA Player Statistics System

A scalable backend system for logging and analyzing NBA player statistics.

## Architecture Overview

The system is built using a clean architecture approach with the following components:

1. **API Layer**: REST endpoints for logging player stats and retrieving aggregate statistics
2. **Service Layer**: Business logic for processing and calculating statistics
3. **Repository Layer**: Data access layer for persistence
4. **Domain Layer**: Core business entities and validation rules

### Key Features

- Real-time player statistics logging
- Aggregate statistics calculation (per player and team)
- High availability and scalability
- Fault-tolerant design
- Containerized deployment

## Technical Stack

- Java 17
- Spring Boot 3.2.3
- PostgreSQL
- Docker & Docker Compose
- Maven

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── nba/
│   │           ├── api/           # REST controllers
│   │           ├── domain/        # Business entities
│   │           ├── repository/    # Data access layer
│   │           ├── service/       # Business logic
│   │           └── NbaStatsApplication.java
│   └── resources/
│       └── application.yml
└── test/                         # Test classes
```

## Setup and Running

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Start the services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. The API will be available at `http://localhost:8080`

## API Endpoints

### Player Statistics

- `POST /api/v1/stats` - Log player statistics for a game
- `GET /api/v1/stats/player/{playerId}` - Get player's season averages
- `GET /api/v1/stats/team/{teamId}` - Get team's season averages

## Data Validation Rules

- Points: Non-negative integer
- Rebounds: Non-negative integer
- Assists: Non-negative integer
- Steals: Non-negative integer
- Blocks: Non-negative integer
- Fouls: Integer (0-6)
- Turnovers: Non-negative integer
- Minutes Played: Float (0-48.0) 