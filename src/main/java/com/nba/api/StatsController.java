package com.nba.api;
import com.nba.service.StatsService;
import com.nba.service.dto.StatsDTO;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping
    public ResponseEntity<Void> logStats(@Valid @RequestBody StatsDTO stats) {
        statsService.logPlayerStats(stats);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/players")
    public ResponseEntity<?> getPlayerStats() {
        try {
            return ResponseEntity.ok(statsService.getPlayersSeasonAverages());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/teams")
    public ResponseEntity<?> getTeamStats() {
        try {
            return ResponseEntity.ok(statsService.getTeamsSeasonAverages());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
} 