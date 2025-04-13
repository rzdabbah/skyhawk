package com.nba.service.impl;

import com.nba.domain.Player;
import com.nba.event.PlayerStatsEvent;
import com.nba.repository.PlayerRepository;
import com.nba.repository.PlayerStatsRepository;
import com.nba.service.PlayerStatsService;
import com.nba.service.PlayerStatsEventPublisher;
import com.nba.service.dto.PlayerStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerStatsServiceImpl implements PlayerStatsService {
    private final PlayerRepository playerRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final PlayerStatsEventPublisher eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = {"playerAverages", "teamAverages"}, allEntries = true)
    public void logPlayerStats(PlayerStatsDTO stats) {
        // Verify player exists
        Player player = playerRepository.findById(stats.getPlayerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        // Create and publish event
        PlayerStatsEvent event = new PlayerStatsEvent();
        event.setPlayerId(player.getId());
        event.setGameDate(LocalDateTime.now());
        event.setPoints(stats.getPoints());
        event.setRebounds(stats.getRebounds());
        event.setAssists(stats.getAssists());
        event.setSteals(stats.getSteals());
        event.setBlocks(stats.getBlocks());
        event.setFouls(stats.getFouls());
        event.setTurnovers(stats.getTurnovers());
        event.setMinutesPlayed(stats.getMinutesPlayed());

        eventPublisher.publishPlayerStatsEvent(event);
    }

    @Override
    @Cacheable(value = "playerAverages", key = "#playerId")
    public Map<String, Double> getPlayerSeasonAverages(Long playerId) {
        return playerStatsRepository.calculatePlayerAverages(playerId);
    }

    @Override
    @Cacheable(value = "teamAverages", key = "#teamId")
    public Map<String, Double> getTeamSeasonAverages(Long teamId) {
        return playerStatsRepository.calculateTeamAverages(teamId);
    }
} 