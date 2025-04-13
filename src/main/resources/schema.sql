-- Drop tables if they exist
DROP TABLE IF EXISTS player_stats;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS teams;

-- Create teams table
CREATE TABLE teams (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    city VARCHAR(100) NOT NULL UNIQUE
);

-- Create players table
CREATE TABLE players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    team_id INTEGER NOT NULL REFERENCES teams(id),
    jersey_number INTEGER,
    UNIQUE (team_id, jersey_number)
);

-- Create player_stats table
CREATE TABLE player_stats (
    id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL REFERENCES players(id),
    game_date TIMESTAMP NOT NULL,
    points INTEGER NOT NULL CHECK (points >= 0),
    rebounds INTEGER NOT NULL CHECK (rebounds >= 0),
    assists INTEGER NOT NULL CHECK (assists >= 0),
    steals INTEGER NOT NULL CHECK (steals >= 0),
    blocks INTEGER NOT NULL CHECK (blocks >= 0),
    fouls INTEGER NOT NULL CHECK (fouls >= 0 AND fouls <= 6),
    turnovers INTEGER NOT NULL CHECK (turnovers >= 0),
    minutes_played FLOAT NOT NULL CHECK (minutes_played >= 0 AND minutes_played <= 48)
);

-- Create indexes
CREATE INDEX idx_player_stats_player_id ON player_stats(player_id);
CREATE INDEX idx_players_team_id ON players(team_id); 