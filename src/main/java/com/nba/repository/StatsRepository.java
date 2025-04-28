package com.nba.repository;

import com.nba.domain.PlayerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.nba.service.dto.PlayerAverageDTO;
import com.nba.service.dto.TeamAverageDTO;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StatsRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PlayerStats> playerStatsRowMapper = (ResultSet rs, int rowNum) -> {
        PlayerStats stats = new PlayerStats();
        stats.setId(rs.getLong("id"));
        stats.setPlayerId(rs.getLong("player_id"));
        stats.setGameId(rs.getLong("game_id"));
        stats.setPoints(rs.getInt("points"));
        stats.setRebounds(rs.getInt("rebounds"));
        stats.setAssists(rs.getInt("assists"));
        stats.setSteals(rs.getInt("steals"));
        stats.setBlocks(rs.getInt("blocks"));
        stats.setFouls(rs.getInt("fouls"));
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
        String sql = "INSERT INTO player_stats (player_id, game_id, points, rebounds, assists, steals, blocks, fouls, turnovers , minutes_played) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,  ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, stats.getPlayerId());
            ps.setLong(2, stats.getGameId());
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
        
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Failed to retrieve generated key for player stats");
        }
        stats.setId(key.longValue());
        return stats;
    }

    private PlayerStats update(PlayerStats stats) {
        String sql = "UPDATE player_stats SET player_id = ?, game_id = ?, points = ?, rebounds = ?, assists = ?, " +
                "steals = ?, blocks = ?, fouls = ?, turnovers = ?, minutes_played = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
                stats.getPlayerId(), 
                stats.getGameId(),
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
        String sql = "INSERT INTO player_stats (player_id, game_id, points, rebounds, assists, " +
                "steals, blocks, fouls, turnovers, minutes_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.batchUpdate(sql, statsList, statsList.size(),
                (ps, stats) -> {
                    ps.setLong(1, stats.getPlayerId());
                    ps.setLong(2, stats.getGameId());
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

  

    public Optional<List<PlayerAverageDTO>> getPlayersSeasonAverages()  throws Exception{
        String sql = """
            SELECT 
    ps.player_id AS player_id,
    p.name AS player_name,
    g.season AS season,
    AVG(ps.points) AS avg_points,
    AVG(ps.rebounds) AS avg_rebounds,
    AVG(ps.assists) AS avg_assists
FROM player_stats ps
JOIN players p ON ps.player_id = p.id
JOIN games g ON ps.game_id = g.id
GROUP BY ps.player_id, p.name, g.season
        """;

        Logger.getLogger("dddd").info(sql);
        List<PlayerAverageDTO> result = jdbcTemplate.query(
            """
            SELECT
                ps.player_id AS player_id,
                p.name AS player_name,
                g.season AS season,
                AVG(ps.points) AS avg_points,
                AVG(ps.rebounds) AS avg_rebounds,
                AVG(ps.assists) AS avg_assists
            FROM player_stats ps
            JOIN players p ON ps.player_id = p.id
            JOIN games g ON ps.game_id = g.id
            GROUP BY ps.player_id, p.name, g.season
            """,
            (rs, rowNum) -> {
                PlayerAverageDTO dto = new PlayerAverageDTO();
        dto.playerId = rs.getLong("player_id");
        dto.playerName = rs.getString("player_name");
        dto.season = rs.getString("season");
        dto.avgPoints = rs.getDouble("avg_points");
        dto.avgRebounds =rs.getDouble("avg_rebounds");
        dto.avgAssists = rs.getDouble("avg_assists");
        Logger.getLogger("SQL").info(  dto.toString());
        return dto;
        }
        );
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
}
        


    public List<TeamAverageDTO> getTeamsSeasonAverages() throws Exception {
        String sql = """
            SELECT t.id AS team_id, t.name AS team_name, g.season,
                   AVG(ps.points) AS avg_points,
                   AVG(ps.rebounds) AS avg_rebounds,
                   AVG(ps.assists) AS avg_assists
            FROM player_stats ps
            JOIN players p ON ps.player_id = p.id
            JOIN teams t ON p.team_id = t.id
            JOIN games g ON ps.game_id = g.id
            GROUP BY t.id, t.name, g.season
        """;

       return null;
    }
} 