package com.nba.repository;

import com.nba.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlayerRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Player> playerRowMapper = (ResultSet rs, int rowNum) -> {
        Player player = new Player();
        player.setId(rs.getLong("id"));
        player.setFirstName(rs.getString("first_name"));
        player.setLastName(rs.getString("last_name"));
        player.setTeamId(rs.getLong("team_id"));
        player.setJerseyNumber(rs.getObject("jersey_number", Integer.class));
        return player;
    };

    public Player save(Player player) {
        if (player.getId() == null) {
            return insert(player);
        } else {
            return update(player);
        }
    }

    private Player insert(Player player) {
        String sql = "INSERT INTO players (first_name, last_name, team_id, jersey_number) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, player.getFirstName());
            ps.setString(2, player.getLastName());
            ps.setLong(3, player.getTeamId());
            ps.setObject(4, player.getJerseyNumber());
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated key for player");
        }
        player.setId(key.longValue());
        return player;
    }

    private Player update(Player player) {
        String sql = "UPDATE players SET first_name = ?, last_name = ?, team_id = ?, jersey_number = ? WHERE id = ?";
        jdbcTemplate.update(sql, player.getFirstName(), player.getLastName(), player.getTeamId(), player.getJerseyNumber(), player.getId());
        return player;
    }

    public Optional<Player> findById(Long id) {
        String sql = "SELECT * FROM players WHERE id = ?";
        List<Player> players = jdbcTemplate.query(sql, playerRowMapper, id);
        return players.isEmpty() ? Optional.empty() : Optional.of(players.get(0));
    }

    public List<Player> findAll() {
        String sql = "SELECT * FROM players";
        return jdbcTemplate.query(sql, playerRowMapper);
    }

    public List<Player> findByTeamId(Long teamId) {
        String sql = "SELECT * FROM players WHERE team_id = ?";
        return jdbcTemplate.query(sql, playerRowMapper, teamId);
    }
} 