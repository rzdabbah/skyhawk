package com.skyhawk.nba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getPlayerStatistics(Long playerId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
            SELECT 
                p.first_name,
                p.last_name,
                t.name as team_name,
                COUNT(DISTINCT ps.game_id) as games_played,
                SUM(ps.points) as total_points,
                AVG(ps.points) as avg_points,
                SUM(ps.rebounds) as total_rebounds,
                AVG(ps.rebounds) as avg_rebounds,
                SUM(ps.assists) as total_assists,
                AVG(ps.assists) as avg_assists,
                SUM(ps.steals) as total_steals,
                SUM(ps.blocks) as total_blocks,
                SUM(ps.minutes_played) as total_minutes
            FROM player_stats ps
            JOIN players p ON ps.player_id = p.id
            JOIN teams t ON p.team_id = t.id
            WHERE p.id = ?
            AND (ps.created_at BETWEEN ? AND ? OR ? IS NULL)
            AND (ps.created_at <= ? OR ? IS NULL)
            GROUP BY p.id, p.first_name, p.last_name, t.name
        """;
        
        return jdbcTemplate.queryForMap(sql, playerId, startDate, endDate, startDate, endDate, endDate);
    }

    public Map<String, Object> getTeamStatistics(Long teamId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
            SELECT 
                t.name,
                t.city,
                COUNT(DISTINCT g.id) as games_played,
                SUM(CASE WHEN g.home_team_id = t.id THEN g.home_team_score ELSE g.away_team_score END) as total_points_scored,
                AVG(CASE WHEN g.home_team_id = t.id THEN g.home_team_score ELSE g.away_team_score END) as avg_points_scored,
                SUM(CASE WHEN g.home_team_id = t.id THEN g.away_team_score ELSE g.home_team_score END) as total_points_allowed,
                AVG(CASE WHEN g.home_team_id = t.id THEN g.away_team_score ELSE g.home_team_score END) as avg_points_allowed
            FROM teams t
            JOIN games g ON t.id = g.home_team_id OR t.id = g.away_team_id
            WHERE t.id = ?
            AND (g.game_date BETWEEN ? AND ? OR ? IS NULL)
            AND (g.game_date <= ? OR ? IS NULL)
            GROUP BY t.id, t.name, t.city
        """;
        
        return jdbcTemplate.queryForMap(sql, teamId, startDate, endDate, startDate, endDate, endDate);
    }

    public Map<String, Object> getGameStatistics(Long gameId) {
        String sql = """
            SELECT 
                g.id,
                ht.name as home_team,
                at.name as away_team,
                g.game_date,
                g.home_team_score,
                g.away_team_score,
                g.status,
                COUNT(ge.id) as total_events,
                COUNT(DISTINCT ge.player_id) as players_involved
            FROM games g
            JOIN teams ht ON g.home_team_id = ht.id
            JOIN teams at ON g.away_team_id = at.id
            LEFT JOIN game_events ge ON g.id = ge.game_id
            WHERE g.id = ?
            GROUP BY g.id, ht.name, at.name, g.game_date, g.home_team_score, g.away_team_score, g.status
        """;
        
        return jdbcTemplate.queryForMap(sql, gameId);
    }

    public List<Map<String, Object>> getGameEvents(Long gameId, String eventType, LocalDateTime startTime, LocalDateTime endTime) {
        String sql = """
            SELECT 
                ge.id,
                ge.event_type,
                ge.event_time,
                p.first_name,
                p.last_name,
                t.name as team_name,
                ge.event_data
            FROM game_events ge
            LEFT JOIN players p ON ge.player_id = p.id
            LEFT JOIN teams t ON ge.team_id = t.id
            WHERE (? IS NULL OR ge.game_id = ?)
            AND (? IS NULL OR ge.event_type = ?)
            AND (ge.event_time BETWEEN ? AND ? OR ? IS NULL)
            AND (ge.event_time <= ? OR ? IS NULL)
            ORDER BY ge.event_time
        """;
        
        return jdbcTemplate.queryForList(sql, 
            gameId, gameId, 
            eventType, eventType,
            startTime, endTime, startTime,
            endTime, endTime);
    }

    public List<Map<String, Object>> getLeaderboard(String statType, LocalDateTime startDate, LocalDateTime endDate, int limit) {
        String column = switch (statType.toLowerCase()) {
            case "points" -> "points";
            case "rebounds" -> "rebounds";
            case "assists" -> "assists";
            case "steals" -> "steals";
            case "blocks" -> "blocks";
            default -> throw new IllegalArgumentException("Invalid stat type: " + statType);
        };

        String sql = """
            SELECT 
                p.first_name,
                p.last_name,
                t.name as team_name,
                SUM(ps.%s) as total_%s,
                AVG(ps.%s) as avg_%s,
                COUNT(DISTINCT ps.game_id) as games_played
            FROM player_stats ps
            JOIN players p ON ps.player_id = p.id
            JOIN teams t ON p.team_id = t.id
            WHERE (ps.created_at BETWEEN ? AND ? OR ? IS NULL)
            AND (ps.created_at <= ? OR ? IS NULL)
            GROUP BY p.id, p.first_name, p.last_name, t.name
            ORDER BY total_%s DESC
            LIMIT ?
        """.formatted(column, column, column, column, column);
        
        return jdbcTemplate.queryForList(sql, 
            startDate, endDate, startDate,
            endDate, endDate,
            limit);
    }
} 