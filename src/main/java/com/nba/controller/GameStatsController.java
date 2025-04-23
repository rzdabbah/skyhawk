package com.nba.controller;

import com.nba.event.GameStatsEvent;
import com.nba.service.GameStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-stats")
@RequiredArgsConstructor
public class GameStatsController {
    private final GameStatsService gameStatsService;

    @PostMapping("/events")
    public ResponseEntity<GameStatsEvent> publishGameStatsEvent(@RequestBody GameStatsEvent event) {
        GameStatsEvent publishedEvent = gameStatsService.publishGameStatsEvent(event);
        return ResponseEntity.ok(publishedEvent);
    }

    @PostMapping("/games/{gameId}/calculate")
    public ResponseEntity<GameStatsEvent> calculateAndPublishGameStats(@PathVariable Long gameId) {
        GameStatsEvent event = gameStatsService.calculateAndPublishGameStats(gameId);
        return ResponseEntity.ok(event);
    }
} 