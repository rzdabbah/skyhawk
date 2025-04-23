-- Insert teams
     (name, city) VALUES
('Lakers', 'Los Angeles'),
('Celtics', 'Boston'),
('Bulls', 'Chicago'),
('Warriors', 'Golden State'),
('Heat', 'Miami');

-- Insert players
INSERT INTO players (name, team_id, jersey_number) VALUES
('LeBron James', 1, 23),
('Anthony Davis', 1, 3),
('Jayson Tatum', 2, 0),
('Jaylen Brown', 2, 7),
('Zach LaVine', 3, 8),
('DeMar DeRozan', 3, 11),
('Stephen Curry', 4, 30),
('Klay Thompson', 4, 11),
('Jimmy Butler', 5, 22),
('Bam Adebayo', 5, 13);

-- Insert games
INSERT INTO games (game_date, home_team_id, away_team_id, home_team_score, away_team_score) VALUES
('2023-10-25 19:30:00', 1, 2, 107, 93),
('2023-10-26 19:30:00', 3, 4, 102, 108),
('2023-10-27 19:30:00', 5, 1, 95, 103),
('2023-10-28 19:30:00', 2, 3, 110, 105),
('2023-10-29 19:30:00', 4, 5, 115, 98);

-- Insert player stats
INSERT INTO player_stats (player_id, game_id, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) VALUES
(1, 1, 25, 10, 8, 1, 1, 2, 3, 36.5),
(2, 1, 18, 12, 2, 0, 3, 3, 2, 32.0),
(3, 1, 22, 7, 5, 2, 0, 1, 4, 38.0),
(4, 1, 20, 6, 4, 1, 0, 2, 3, 35.0),
(5, 2, 28, 5, 6, 2, 0, 1, 2, 37.0),
(6, 2, 24, 4, 5, 1, 0, 2, 3, 36.0),
(7, 2, 32, 5, 7, 1, 0, 1, 4, 38.0),
(8, 2, 18, 3, 2, 1, 0, 2, 1, 32.0),
(9, 3, 22, 8, 6, 2, 1, 3, 3, 36.0),
(10, 3, 16, 10, 3, 0, 2, 2, 2, 34.0),
(1, 3, 30, 8, 9, 2, 1, 1, 4, 38.0),
(2, 3, 20, 14, 3, 0, 4, 2, 2, 35.0),
(3, 4, 26, 8, 6, 1, 0, 2, 3, 37.0),
(4, 4, 24, 7, 5, 2, 0, 1, 2, 36.0),
(5, 4, 18, 4, 5, 1, 0, 2, 3, 34.0),
(6, 4, 22, 5, 6, 1, 0, 1, 2, 35.0),
(7, 5, 35, 6, 8, 1, 0, 1, 3, 38.0),
(8, 5, 20, 4, 3, 1, 0, 2, 1, 32.0),
(9, 5, 18, 7, 5, 2, 0, 2, 2, 35.0),
(10, 5, 14, 12, 4, 0, 3, 3, 1, 33.0); 