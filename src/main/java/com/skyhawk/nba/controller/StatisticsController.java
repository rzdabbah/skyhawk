package com.skyhawk.nba.controller;

import com.skyhawk.nba.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/player/{playerId}")
    public ResponseEntity<?> getPlayerStatistics(
            @PathVariable Long playerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(statisticsService.getPlayerStatistics(playerId, startDate, endDate));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getTeamStatistics(
            @PathVariable Long teamId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(statisticsService.getTeamStatistics(teamId, startDate, endDate));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> getGameStatistics(@PathVariable Long gameId) {
        return ResponseEntity.ok(statisticsService.getGameStatistics(gameId));
    }

    @GetMapping("/events")
    public ResponseEntity<?> getGameEvents(
            @RequestParam(required = false) Long gameId,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(statisticsService.getGameEvents(gameId, eventType, startTime, endTime));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard(
            @RequestParam String statType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statisticsService.getLeaderboard(statType, startDate, endDate, limit));
    }
} 