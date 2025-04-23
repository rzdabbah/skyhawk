package com.nba.service.impl;

import com.nba.domain.Player;
import com.nba.repository.PlayerRepository;
import com.nba.repository.TeamRepository;
import com.nba.service.PlayerService;
import com.nba.service.dto.PlayerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public Player createPlayer(PlayerDTO playerDTO) {
        // Verify team exists
        teamRepository.findById(playerDTO.getTeamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));

        Player player = new Player();
        player.setName(playerDTO.getFirstName() + " " + playerDTO.getLastName());
        player.setTeamId(playerDTO.getTeamId());
        player.setJerseyNumber(playerDTO.getJerseyNumber());

        return playerRepository.save(player);
    }

    @Override
    public Player getPlayer(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> getPlayersByTeam(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }
} 