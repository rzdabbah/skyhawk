# NBA Player Statistics System

A scalable, event-driven backend system for logging and analyzing NBA player statistics.

## Architecture Overview

The system is built using a clean architecture approach with the following components:

1. **API Layer**: REST endpoints for logging player stats and retrieving aggregate statistics
2. **Service Layer**: Business logic for processing and calculating statistics
3. **Repository Layer**: Data access layer for persistence
4. **Domain Layer**: Core business entities and validation rules
5. **Event Layer**: Event-driven communication using Kafka

### Key Features

- Real-time player statistics logging
- Event-driven architecture using Kafka
- Aggregate statistics calculation (per player and team)
- High availability and scalability
- Fault-tolerant design
- Containerized deployment
- Automatic team and player creation
- Game statistics tracking

## Technical Stack

- Java 17
- Spring Boot 3.2.3
- PostgreSQL 15
- Apache Kafka
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
│   │           ├── config/        # Configuration classes
│   │           ├── domain/        # Business entities
│   │           ├── event/         # Event classes
│   │           ├── repository/    # Data access layer
│   │           ├── service/       # Business logic
│   │           └── NbaStatsApplication.java
│   └── resources/
│       ├── application.yml       # Application configuration
│       ├── schema.sql           # Database schema
│       └── data.sql             # Initial data
└── test/                        # Test classes
```

## Setup and Running

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd skyhawk
   ```

2. Create a `.env` file from the template:
   ```bash
   cp .env.example .env
   ```

3. Build and start the services:
   ```bash
   ./manage-maven-cache.sh clean  # Clean Maven cache
   docker compose up --build -d   # Build and start services
   ```

4. The API will be available at `http://localhost:8080`

## Services

The system consists of the following services:

1. **App Service**: Main application service handling API requests
2. **Stats Consumer**: Service consuming player statistics events
3. **Database**: PostgreSQL database
4. **Kafka**: Message broker for event-driven communication
5. **Zookeeper**: Required for Kafka operation

## API Endpoints

### Player Statistics

- `POST /api/v1/stats` - Log player statistics for a game
- `GET /api/v1/stats/player/{playerId}` - Get player's season averages
- `GET /api/v1/stats/team/{teamId}` - Get team's season averages

### Game Statistics

- `GET /api/v1/games/{gameId}/stats` - Get game statistics
- `GET /api/v1/games/{gameId}/players` - Get player statistics for a game

## Event Topics

- `nba.player-stats`: Player statistics events
- `nba.game-stats`: Game statistics events
- `nba.games`: Game events
- `nba.teams`: Team events

## Data Validation Rules

- Points: Non-negative integer
- Rebounds: Non-negative integer
- Assists: Non-negative integer
- Steals: Non-negative integer
- Blocks: Non-negative integer
- Fouls: Integer (0-6)
- Turnovers: Non-negative integer
- Minutes Played: Float (0-48.0)

## Error Handling

The system includes comprehensive error handling:
- Foreign key constraint violations
- Null pointer exceptions
- Event processing errors
- Database transaction management

## Monitoring

- Application metrics available via Spring Boot Actuator
- Kafka consumer group monitoring
- Database connection monitoring

## Development

For local development:
1. Use the provided Docker Compose configuration
2. The database will be automatically initialized with schema and sample data
3. Kafka topics will be automatically created
4. Use the Maven cache management script for faster builds

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request 