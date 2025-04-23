package com.nba.service;

import com.nba.domain.Game;
import com.nba.service.dto.GameDTO;
import java.util.Optional;

public interface GameService {
    Game createGame(GameDTO gameDTO);
    Optional<Game> getGame(Long id);
} 