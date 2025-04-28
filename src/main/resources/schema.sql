-- Create teams table
DROP TABLE IF EXISTS teams cascade;
DROP TABLE IF EXISTS players cascade;
DROP TABLE IF EXISTS games cascade;
DROP TABLE IF EXISTS game_events cascade;
DROP TABLE IF EXISTS player_stats cascade;




CREATE TABLE IF NOT EXISTS teams (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    conference VARCHAR(50),
    division VARCHAR(50),
    founded_year INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create players table
CREATE TABLE IF NOT EXISTS players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    team_id INTEGER REFERENCES teams(id),
    jersey_number INTEGER,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

-- Create games table
CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    home_team_id INTEGER REFERENCES teams(id),
    away_team_id INTEGER REFERENCES teams(id),
    game_date TIMESTAMP WITH TIME ZONE NOT NULL,
    home_team_score INTEGER,
    away_team_score INTEGER,
    season VARCHAR(10),
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (home_team_id) REFERENCES  teams(id),
    FOREIGN KEY (away_team_id) REFERENCES  teams(id)

);

-- Create events table for tracking all game events
CREATE TABLE IF NOT EXISTS game_events (
    id SERIAL PRIMARY KEY,
    game_id INTEGER REFERENCES games(id),
    event_type VARCHAR(50) NOT NULL,
    event_time TIMESTAMP WITH TIME ZONE NOT NULL,
    player_id INTEGER REFERENCES players(id),
    team_id INTEGER REFERENCES teams(id),
    event_data JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- Create player_stats table with event-based updates
CREATE TABLE IF NOT EXISTS player_stats (
    id SERIAL PRIMARY KEY,
    player_id INTEGER REFERENCES players(id),
    game_id INTEGER REFERENCES games(id),
    points INTEGER DEFAULT 0,
    rebounds INTEGER DEFAULT 0,
    assists INTEGER DEFAULT 0,
    steals INTEGER DEFAULT 0,
    blocks INTEGER DEFAULT 0,
    fouls INTEGER DEFAULT 0,
    turnovers INTEGER DEFAULT 0,
    minutes_played INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(id),
    FOREIGN KEY (game_id) REFERENCES games(id)

);

-- Create indexes for better query performance
CREATE INDEX idx_game_events_game_id ON game_events(game_id);
CREATE INDEX idx_game_events_event_time ON game_events(event_time);
CREATE INDEX idx_game_events_event_type ON game_events(event_type);
CREATE INDEX idx_player_stats_game_id ON player_stats(game_id);
CREATE INDEX idx_player_stats_player_id ON player_stats(player_id);

