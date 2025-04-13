package com.nba.repository;

import com.nba.domain.PlayerStats;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PlayerStatsRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PlayerStats> playerStatsRowMapper = (ResultSet rs, int rowNum) -> {
        PlayerStats stats = new PlayerStats();
        stats.setId(rs.getLong("id"));
        stats.setPlayerId(rs.getLong("player_id"));
        stats.setGameDate(rs.getTimestamp("game_date").toLocalDateTime());
        stats.setPoints(rs.getInt("points"));
        stats.setRebounds(rs.getInt("rebounds"));
        stats.setAssists(rs.getInt("assists"));
        stats.setSteals(rs.getInt("steals"));
        stats.setBlocks(rs.getInt("blocks"));
        stats.setFouls(rs.getInt("fouls"));
        stats.setTurnovers(rs.getInt("turnovers"));
        stats.setMinutesPlayed(rs.getFloat("minutes_played"));
        return stats;
    };

    public PlayerStats save(PlayerStats stats) {
        if (stats.getId() == null) {
            return insert(stats);
        } else {
            return update(stats);
        }
    }

    private PlayerStats insert(PlayerStats stats) {
        String sql = "INSERT INTO player_stats (player_id, game_date, points, rebounds, assists, steals, blocks, fouls, turnovers, minutes_played) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, stats.getPlayerId());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(stats.getGameDate()));
            ps.setInt(3, stats.getPoints());
            ps.setInt(4, stats.getRebounds());
            ps.setInt(5, stats.getAssists());
            ps.setInt(6, stats.getSteals());
            ps.setInt(7, stats.getBlocks());
            ps.setInt(8, stats.getFouls());
            ps.setInt(9, stats.getTurnovers());
            ps.setFloat(10, stats.getMinutesPlayed());
            return ps;
        }, keyHolder);
        
        stats.setId(keyHolder.getKey().longValue());
        return stats;
    }

    private PlayerStats update(PlayerStats stats) {
        String sql = "UPDATE player_stats SET player_id = ?, game_date = ?, points = ?, rebounds = ?, assists = ?, " +
                "steals = ?, blocks = ?, fouls = ?, turnovers = ?, minutes_played = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
                stats.getPlayerId(), 
                java.sql.Timestamp.valueOf(stats.getGameDate()),
                stats.getPoints(), 
                stats.getRebounds(), 
                stats.getAssists(), 
                stats.getSteals(), 
                stats.getBlocks(), 
                stats.getFouls(), 
                stats.getTurnovers(), 
                stats.getMinutesPlayed(),
                stats.getId());
        return stats;
    }

    public Optional<PlayerStats> findById(Long id) {
        String sql = "SELECT * FROM player_stats WHERE id = ?";
        List<PlayerStats> stats = jdbcTemplate.query(sql, playerStatsRowMapper, id);
        return stats.isEmpty() ? Optional.empty() : Optional.of(stats.get(0));
    }

    public List<PlayerStats> findByPlayerId(Long playerId) {
        String sql = "SELECT * FROM player_stats WHERE player_id = ?";
        return jdbcTemplate.query(sql, playerStatsRowMapper, playerId);
    }

    public Map<String, Double> calculatePlayerAverages(Long playerId) {
        String sql = "SELECT " +
                "AVG(points) as points, " +
                "AVG(rebounds) as rebounds, " +
                "AVG(assists) as assists, " +
                "AVG(steals) as steals, " +
                "AVG(blocks) as blocks, " +
                "AVG(fouls) as fouls, " +
                "AVG(turnovers) as turnovers, " +
                "AVG(minutes_played) as minutes_played " +
                "FROM player_stats " +
                "WHERE player_id = ?";
        
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, playerId);
        return result.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> ((Number) e.getValue()).doubleValue()
                ));
    }

    public Map<String, Double> calculateTeamAverages(Long teamId) {
        String sql = "SELECT " +
                "AVG(ps.points) as points, " +
                "AVG(ps.rebounds) as rebounds, " +
                "AVG(ps.assists) as assists, " +
                "AVG(ps.steals) as steals, " +
                "AVG(ps.blocks) as blocks, " +
                "AVG(ps.fouls) as fouls, " +
                "AVG(ps.turnovers) as turnovers, " +
                "AVG(ps.minutes_played) as minutes_played " +
                "FROM player_stats ps " +
                "JOIN players p ON ps.player_id = p.id " +
                "WHERE p.team_id = ?";
        
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, teamId);
        return result.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> ((Number) e.getValue()).doubleValue()
                ));
    }

    public void saveAll(List<PlayerStats> statsList) {
        String sql = "INSERT INTO player_stats (player_id, game_date, points, rebounds, assists, " +
                "steals, blocks, fouls, turnovers, minutes_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.batchUpdate(sql, statsList, statsList.size(),
                (ps, stats) -> {
                    ps.setLong(1, stats.getPlayerId());
                    ps.setTimestamp(2, java.sql.Timestamp.valueOf(stats.getGameDate()));
                    ps.setInt(3, stats.getPoints());
                    ps.setInt(4, stats.getRebounds());
                    ps.setInt(5, stats.getAssists());
                    ps.setInt(6, stats.getSteals());
                    ps.setInt(7, stats.getBlocks());
                    ps.setInt(8, stats.getFouls());
                    ps.setInt(9, stats.getTurnovers());
                    ps.setFloat(10, stats.getMinutesPlayed());
                });
    }
} 