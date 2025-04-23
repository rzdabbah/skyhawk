-- Drop tables if they exist
DROP TABLE IF EXISTS player_stats;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS games;

-- Create teams table
CREATE TABLE teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    city VARCHAR(100) NOT NULL UNIQUE
);

-- Create players table
CREATE TABLE players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    team_id BIGINT NOT NULL REFERENCES teams(id),
    jersey_number INTEGER,
    UNIQUE (team_id, jersey_number)
);

-- Create games table
CREATE TABLE games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_date DATE NOT NULL,
    home_team_id BIGINT NOT NULL REFERENCES teams(id),
    away_team_id BIGINT NOT NULL REFERENCES teams(id),
    home_team_score INTEGER NOT NULL CHECK (home_team_score >= 0),
    away_team_score INTEGER NOT NULL CHECK (away_team_score >= 0)
);

-- Create player_stats table
CREATE TABLE player_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL REFERENCES players(id),
    game_id BIGINT NOT NULL REFERENCES games(id),
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
CREATE INDEX idx_player_stats_game_id ON player_stats(game_id);
CREATE INDEX idx_players_team_id ON players(team_id);
CREATE INDEX idx_games_home_team_id ON games(home_team_id);
CREATE INDEX idx_games_away_team_id ON games(away_team_id);
CREATE INDEX idx_games_game_date ON games(game_date); 