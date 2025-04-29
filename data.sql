-- Insert sample teams
INSERT INTO teams (name, city, conference, division, founded_year) VALUES
    ('Lakers', 'Los Angeles', 'West', 'Pacific', 1947),
    ('Celtics', 'Boston', 'East', 'Atlantic', 1946),
    ('Bulls', 'Chicago', 'East', 'Central', 1966),
    ('Warriors', 'Golden State', 'West', 'Pacific', 1946);

-- Insert sample players
INSERT INTO players (first_name, last_name, team_id, position, height_cm, weight_kg, jersey_number) VALUES
    ('LeBron', 'James', 1, 'Forward', 206, 113, 23),
    ('Stephen', 'Curry', 4, 'Guard', 188, 84, 30),
    ('Jayson', 'Tatum', 2, 'Forward', 203, 95, 0),
    ('DeMar', 'DeRozan', 3, 'Guard-Forward', 198, 100, 11);

-- Insert sample games
INSERT INTO games (home_team_id, away_team_id, game_date, home_team_score, away_team_score, season, status) VALUES
    (1, 2, '2024-03-15 19:30:00-07', 120, 115, '2023-24', 'COMPLETED'),
    (3, 4, '2024-04-15 19:30:00-07', 111, 112, '2023-24', 'COMPLETED');

-- Insert sample game events
INSERT INTO game_events (game_id, event_type, event_time, player_id, team_id, event_data) VALUES
    -- Game 1 Events (Lakers vs Celtics)
    (1, 'GAME_START', '2024-03-15 19:30:00-07', NULL, 1, '{"quarter": 1, "time_remaining": "12:00"}'),
    (1, 'SHOT_MADE', '2024-03-15 19:31:15-07', 1, 1, '{"points": 2, "shot_type": "JUMP_SHOT", "location": {"x": 120, "y": 45}, "assisted_by": null}'),
    (1, 'SHOT_MADE', '2024-03-15 19:31:30-07', 3, 2, '{"points": 3, "shot_type": "THREE_POINT", "location": {"x": 180, "y": 45}, "assisted_by": null}'),
    (1, 'REBOUND', '2024-03-15 19:31:45-07', 1, 1, '{"rebound_type": "DEFENSIVE", "location": {"x": 90, "y": 90}}'),
    (1, 'ASSIST', '2024-03-15 19:32:00-07', 1, 1, '{"assisted_player": 2, "points_scored": 2}'),
    (1, 'SUBSTITUTION', '2024-03-15 19:35:00-07', NULL, 1, '{"player_in": 2, "player_out": 3, "quarter": 1, "time_remaining": "07:00"}'),
    (1, 'TIMEOUT', '2024-03-15 19:40:00-07', NULL, 2, '{"timeout_type": "FULL", "quarter": 1, "time_remaining": "02:00"}'),
    (1, 'GAME_END', '2024-03-15 22:15:00-07', NULL, 1, '{"final_score": {"home": 120, "away": 115}}'),

    -- Game 2 Events (Bulls vs Warriors)
    (2, 'GAME_START', '2024-03-15 19:30:00-07', NULL, 3, '{"quarter": 1, "time_remaining": "12:00"}'),
    (2, 'SHOT_MADE', '2024-03-15 19:31:20-07', 2, 4, '{"points": 3, "shot_type": "THREE_POINT", "location": {"x": 180, "y": 45}, "assisted_by": null}'),
    (2, 'STEAL', '2024-03-15 19:32:00-07', 4, 3, '{"stolen_from": 2, "location": {"x": 150, "y": 75}}'),
    (2, 'BLOCK', '2024-03-15 19:32:30-07', 4, 3, '{"blocked_player": 2, "location": {"x": 120, "y": 90}}'),
    (2, 'FOUL', '2024-03-15 19:33:00-07', 2, 4, '{"foul_type": "SHOOTING", "fouled_player": 4, "free_throws_awarded": 2}'),
    (2, 'GAME_END', '2024-03-15 22:15:00-07', NULL, 4, '{"final_score": {"home": 108, "away": 112}}');

-- Insert initial player stats (will be updated by events)
INSERT INTO player_stats (player_id, game_id, points, rebounds, assists, steals, blocks, minutes_played) VALUES
    (1, 1, 25, 8, 10, 2, 1, 36),
    (2, 1, 30, 5, 8, 1, 0, 34),
    (3, 1, 28, 7, 5, 1, 2, 35),
    (4, 2, 22, 6, 7, 1, 0, 32); 