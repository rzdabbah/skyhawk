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
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    team_id BIGINT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(id),
    UNIQUE (team_id)
);

-- Create games table
CREATE TABLE games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_date TIMESTAMP NOT NULL,
    home_team_id BIGINT NOT NULL,
    away_team_id BIGINT NOT NULL,
    home_team_score INTEGER DEFAULT 0,
    away_team_score INTEGER DEFAULT 0,
    FOREIGN KEY (home_team_id) REFERENCES teams(id),
    FOREIGN KEY (away_team_id) REFERENCES teams(id),
    CHECK (home_team_score >= 0),
    CHECK (away_team_score >= 0)
);

-- Create player_stats table
CREATE TABLE player_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    points INTEGER NOT NULL,
    rebounds INTEGER NOT NULL,
    assists INTEGER NOT NULL,
    steals INTEGER NOT NULL,
    blocks INTEGER NOT NULL,
    fouls INTEGER NOT NULL,
    turnovers INTEGER NOT NULL,
    minutes_played FLOAT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id),
    FOREIGN KEY (game_id) REFERENCES games(id),
    CHECK (points >= 0),
    CHECK (rebounds >= 0),
    CHECK (assists >= 0),
    CHECK (steals >= 0),
    CHECK (blocks >= 0),
    CHECK (fouls >= 0 AND fouls <= 6),
    CHECK (turnovers >= 0),
    CHECK (minutes_played >= 0 AND minutes_played <= 48)
);

-- Create indexes
CREATE INDEX idx_player_stats_player_id ON player_stats(player_id);
CREATE INDEX idx_player_stats_game_id ON player_stats(game_id);
CREATE INDEX idx_players_team_id ON players(team_id);
CREATE INDEX idx_games_home_team_id ON games(home_team_id);
CREATE INDEX idx_games_away_team_id ON games(away_team_id);
CREATE INDEX idx_games_game_date ON games(game_date); 