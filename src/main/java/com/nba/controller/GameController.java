package com.nba.controller;

import com.nba.domain.Game;
import com.nba.service.GameService;
import com.nba.service.dto.GameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody GameDTO gameDTO) {
        Game game = gameService.createGame(gameDTO);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 