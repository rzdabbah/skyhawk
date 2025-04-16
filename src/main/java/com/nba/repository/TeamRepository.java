package com.nba.repository;

import com.nba.domain.Team;
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
public class TeamRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Team> teamRowMapper = (ResultSet rs, int rowNum) -> {
        Team team = new Team();
        team.setId(rs.getLong("id"));
        team.setName(rs.getString("name"));
        team.setCity(rs.getString("city"));
        return team;
    };

    public Team save(Team team) {
        if (team.getId() == null) {
            return insert(team);
        } else {
            return update(team);
        }
    }

    private Team insert(Team team) {
        String sql = "INSERT INTO teams (name, city) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, team.getName());
            ps.setString(2, team.getCity());
            return ps;
        }, keyHolder);
        
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated key for team");
        }
        team.setId(key.longValue());
        return team;
    }

    private Team update(Team team) {
        String sql = "UPDATE teams SET name = ?, city = ? WHERE id = ?";
        jdbcTemplate.update(sql, team.getName(), team.getCity(), team.getId());
        return team;
    }

    public Optional<Team> findById(Long id) {
        String sql = "SELECT * FROM teams WHERE id = ?";
        List<Team> teams = jdbcTemplate.query(sql, teamRowMapper, id);
        return teams.isEmpty() ? Optional.empty() : Optional.of(teams.get(0));
    }

    public List<Team> findAll() {
        String sql = "SELECT * FROM teams";
        return jdbcTemplate.query(sql, teamRowMapper);
    }
} 