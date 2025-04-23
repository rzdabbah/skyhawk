package com.nba.service.impl;

import com.nba.domain.Game;
import com.nba.repository.GameRepository;
import com.nba.repository.TeamRepository;
import com.nba.service.GameService;
import com.nba.service.dto.GameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    @Override
    public Game createGame(GameDTO gameDTO) {
        // Validate teams exist
        teamRepository.findById(gameDTO.getHomeTeamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Home team not found"));
        teamRepository.findById(gameDTO.getAwayTeamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Away team not found"));

        Game game = new Game();
        game.setGameDate(gameDTO.getGameDate());
        game.setHomeTeamId(gameDTO.getHomeTeamId());
        game.setAwayTeamId(gameDTO.getAwayTeamId());
        game.setHomeTeamScore(gameDTO.getHomeTeamScore() != null ? gameDTO.getHomeTeamScore() : 0);
        game.setAwayTeamScore(gameDTO.getAwayTeamScore() != null ? gameDTO.getAwayTeamScore() : 0);

        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> getGame(Long id) {
        return gameRepository.findById(id);
    }
} 