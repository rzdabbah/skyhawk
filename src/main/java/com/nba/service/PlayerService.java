package com.nba.service;

import com.nba.domain.Player;
import com.nba.service.dto.PlayerDTO;

import java.util.List;

public interface PlayerService {
    Player createPlayer(PlayerDTO playerDTO);
    Player getPlayer(Long id);
    List<Player> getAllPlayers();
    List<Player> getPlayersByTeam(Long teamId);
} 