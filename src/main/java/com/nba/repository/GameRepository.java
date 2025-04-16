package com.nba.repository;

import com.nba.domain.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class GameRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Game> gameRowMapper = (rs, rowNum) -> {
        Game game = new Game();
        game.setId(rs.getLong("id"));
        game.setGameDate(rs.getTimestamp("game_date").toLocalDateTime());
        game.setHomeTeamId(rs.getLong("home_team_id"));
        game.setAwayTeamId(rs.getLong("away_team_id"));
        game.setHomeTeamScore(rs.getInt("home_team_score"));
        game.setAwayTeamScore(rs.getInt("away_team_score"));
        return game;
    };

    public GameRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Game> findByGameDate(LocalDateTime gameDate) {
        String sql = "SELECT * FROM games WHERE game_date = ?";
        return jdbcTemplate.query(sql, gameRowMapper, gameDate)
                .stream()
                .findFirst();
    }

    public Game save(Game game) {
        if (game.getId() == null) {
            return insert(game);
        } else {
            return update(game);
        }
    }

    private Game insert(Game game) {
        String sql = "INSERT INTO games (game_date, home_team_id, away_team_id, home_team_score, away_team_score) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING *";
        return jdbcTemplate.queryForObject(sql, gameRowMapper,
                game.getGameDate(),
                game.getHomeTeamId(),
                game.getAwayTeamId(),
                game.getHomeTeamScore(),
                game.getAwayTeamScore());
    }

    private Game update(Game game) {
        String sql = "UPDATE games SET game_date = ?, home_team_id = ?, away_team_id = ?, " +
                    "home_team_score = ?, away_team_score = ? WHERE id = ? RETURNING *";
        return jdbcTemplate.queryForObject(sql, gameRowMapper,
                game.getGameDate(),
                game.getHomeTeamId(),
                game.getAwayTeamId(),
                game.getHomeTeamScore(),
                game.getAwayTeamScore(),
                game.getId());
    }
} 