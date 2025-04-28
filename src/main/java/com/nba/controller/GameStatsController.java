package com.nba.controller;

import com.nba.event.GameStatsEvent;
import com.nba.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-stats")
@RequiredArgsConstructor
public class GameStatsController {
    private final StatsService statsService;

    @PostMapping("/events")
    public ResponseEntity<GameStatsEvent> publishGameStatsEvent(@RequestBody GameStatsEvent event) {
        GameStatsEvent publishedEvent = statsService.publishGameStatsEvent(event);
        return ResponseEntity.ok(publishedEvent);
    }

   
} 