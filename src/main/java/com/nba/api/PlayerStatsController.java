package com.nba.api;
import com.nba.service.PlayerStatsService;
import com.nba.service.dto.PlayerStatsDTO;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class PlayerStatsController {
    private final PlayerStatsService playerStatsService;

    @PostMapping
    public ResponseEntity<Void> logPlayerStats(@Valid @RequestBody PlayerStatsDTO stats) {
        playerStatsService.logPlayerStats(stats);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<Map<String, Double>> getPlayerSeasonAverages(@PathVariable Long playerId) {
        return ResponseEntity.ok(playerStatsService.getPlayerSeasonAverages(playerId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<Map<String, Double>> getTeamSeasonAverages(@PathVariable Long teamId) {
        return ResponseEntity.ok(playerStatsService.getTeamSeasonAverages(teamId));
    }
} 