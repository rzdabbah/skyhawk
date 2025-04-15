package com.skyhawk.nba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getPlayerStatistics(Long playerId, LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT p.first_name, p.last_name, t.name as team_name, ")
                 .append("COUNT(DISTINCT ps.game_id) as games_played, ")
                 .append("SUM(ps.points) as total_points, ")
                 .append("AVG(ps.points) as avg_points, ")
                 .append("SUM(ps.rebounds) as total_rebounds, ")
                 .append("AVG(ps.rebounds) as avg_rebounds, ")
                 .append("SUM(ps.assists) as total_assists, ")
                 .append("AVG(ps.assists) as avg_assists, ")
                 .append("SUM(ps.steals) as total_steals, ")
                 .append("SUM(ps.blocks) as total_blocks, ")
                 .append("SUM(ps.minutes_played) as total_minutes ")
                 .append("FROM player_stats ps ")
                 .append("JOIN players p ON ps.player_id = p.id ")
                 .append("JOIN teams t ON p.team_id = t.id ")
                 .append("JOIN games g ON ps.game_id = g.id ")
                 .append("WHERE p.id = ? ");

        List<Object> params = new ArrayList<>();
        List<Integer> types = new ArrayList<>();

        params.add(playerId);
        types.add(java.sql.Types.BIGINT);

        if (startDate != null) {
            sqlBuilder.append("AND g.game_date >= ? ");
            params.add(Timestamp.valueOf(startDate));
            types.add(java.sql.Types.TIMESTAMP);
        }

        if (endDate != null) {
            sqlBuilder.append("AND g.game_date <= ? ");
            params.add(Timestamp.valueOf(endDate));
            types.add(java.sql.Types.TIMESTAMP);
        }

        sqlBuilder.append("GROUP BY p.id, p.first_name, p.last_name, t.name");

        return jdbcTemplate.queryForMap(
            sqlBuilder.toString(),
            params.toArray(),
            types.stream().mapToInt(Integer::intValue).toArray()
        );
    }

    public Map<String, Object> getTeamStatistics(Long teamId, LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT t.name, t.city, ")
                 .append("COUNT(DISTINCT g.id) as games_played, ")
                 .append("SUM(CASE WHEN g.home_team_id = t.id THEN g.home_team_score ELSE g.away_team_score END) as total_points_scored, ")
                 .append("AVG(CASE WHEN g.home_team_id = t.id THEN g.home_team_score ELSE g.away_team_score END) as avg_points_scored, ")
                 .append("SUM(CASE WHEN g.home_team_id = t.id THEN g.away_team_score ELSE g.home_team_score END) as total_points_allowed, ")
                 .append("AVG(CASE WHEN g.home_team_id = t.id THEN g.away_team_score ELSE g.home_team_score END) as avg_points_allowed ")
                 .append("FROM teams t ")
                 .append("JOIN games g ON t.id = g.home_team_id OR t.id = g.away_team_id ")
                 .append("WHERE t.id = ? ");

        List<Object> params = new ArrayList<>();
        List<Integer> types = new ArrayList<>();

        params.add(teamId);
        types.add(java.sql.Types.BIGINT);

        if (startDate != null) {
            sqlBuilder.append("AND g.game_date >= ? ");
            params.add(Timestamp.valueOf(startDate));
            types.add(java.sql.Types.TIMESTAMP);
        }

        if (endDate != null) {
            sqlBuilder.append("AND g.game_date <= ? ");
            params.add(Timestamp.valueOf(endDate));
            types.add(java.sql.Types.TIMESTAMP);
        }

        sqlBuilder.append("GROUP BY t.id, t.name, t.city");

        return jdbcTemplate.queryForMap(
            sqlBuilder.toString(),
            params.toArray(),
            types.stream().mapToInt(Integer::intValue).toArray()
        );
    }

    public Map<String, Object> getGameStatistics(Long gameId) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT g.id, g.game_date, ")
                 .append("ht.name as home_team, at.name as away_team, ")
                 .append("g.home_team_score, g.away_team_score, ")
                 .append("COUNT(DISTINCT ps.player_id) as players_involved ")
                 .append("FROM games g ")
                 .append("JOIN teams ht ON g.home_team_id = ht.id ")
                 .append("JOIN teams at ON g.away_team_id = at.id ")
                 .append("LEFT JOIN player_stats ps ON g.id = ps.game_id ")
                 .append("WHERE g.id = ? ")
                 .append("GROUP BY g.id, g.game_date, ht.name, at.name, g.home_team_score, g.away_team_score");

        List<Object> params = new ArrayList<>();
        List<Integer> types = new ArrayList<>();

        params.add(gameId);
        types.add(java.sql.Types.BIGINT);

        return jdbcTemplate.queryForMap(
            sqlBuilder.toString(),
            params.toArray(),
            types.stream().mapToInt(Integer::intValue).toArray()
        );
    }

    public List<Map<String, Object>> getGameEvents(Long gameId, String eventType, LocalDateTime startTime, LocalDateTime endTime) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ge.id, ge.event_type, ge.event_time, ")
                 .append("p.first_name, p.last_name, t.name as team_name, ")
                 .append("ge.event_data ")
                 .append("FROM game_events ge ")
                 .append("LEFT JOIN players p ON ge.player_id = p.id ")
                 .append("LEFT JOIN teams t ON ge.team_id = t.id ")
                 .append("WHERE ge.game_id = ? ");

        List<Object> params = new ArrayList<>();
        List<Integer> types = new ArrayList<>();

        params.add(gameId);
        types.add(java.sql.Types.BIGINT);

        if (eventType != null && !eventType.isEmpty()) {
            sqlBuilder.append("AND ge.event_type = ? ");
            params.add(eventType);
            types.add(java.sql.Types.VARCHAR);
        }

        if (startTime != null) {
            sqlBuilder.append("AND ge.event_time >= ? ");
            params.add(Timestamp.valueOf(startTime));
            types.add(java.sql.Types.TIMESTAMP);
        }

        if (endTime != null) {
            sqlBuilder.append("AND ge.event_time <= ? ");
            params.add(Timestamp.valueOf(endTime));
            types.add(java.sql.Types.TIMESTAMP);
        }

        sqlBuilder.append("ORDER BY ge.event_time");

        return jdbcTemplate.queryForList(
            sqlBuilder.toString(),
            params.toArray(),
            types.stream().mapToInt(Integer::intValue).toArray()
        );
    }

    public List<Map<String, Object>> getLeaderboard(String statType, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        String column = switch (statType.toLowerCase()) {
            case "points" -> "points";
            case "rebounds" -> "rebounds";
            case "assists" -> "assists";
            case "steals" -> "steals";
            case "blocks" -> "blocks";
            case "minutes" -> "minutes_played";
            default -> throw new IllegalArgumentException("Invalid stat type: " + statType);
        };

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT p.first_name, p.last_name, t.name as team_name, ")
                 .append("SUM(ps.").append(column).append(") as total_").append(column).append(", ")
                 .append("AVG(ps.").append(column).append(") as avg_").append(column).append(", ")
                 .append("COUNT(DISTINCT ps.game_id) as games_played ")
                 .append("FROM player_stats ps ")
                 .append("JOIN players p ON ps.player_id = p.id ")
                 .append("JOIN teams t ON p.team_id = t.id ")
                 .append("JOIN games g ON ps.game_id = g.id ")
                 .append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        List<Integer> types = new ArrayList<>();

        if (startDate != null) {
            sqlBuilder.append("AND g.game_date >= ? ");
            params.add(Timestamp.valueOf(startDate));
            types.add(java.sql.Types.TIMESTAMP);
        }

        if (endDate != null) {
            sqlBuilder.append("AND g.game_date <= ? ");
            params.add(Timestamp.valueOf(endDate));
            types.add(java.sql.Types.TIMESTAMP);
        }

        sqlBuilder.append("GROUP BY p.id, p.first_name, p.last_name, t.name ")
                 .append("ORDER BY total_").append(column).append(" DESC ")
                 .append("LIMIT ?");
        
        params.add(limit);
        types.add(java.sql.Types.INTEGER);

        return jdbcTemplate.queryForList(
            sqlBuilder.toString(), 
            params.toArray(), 
            types.stream().mapToInt(Integer::intValue).toArray()
        );
    }
} 