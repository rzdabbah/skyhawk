package com.nba.service;

import com.nba.domain.PlayerStats;
import com.nba.event.PlayerStatsEvent;
import com.nba.repository.PlayerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PlayerStatsEventListener {
    private final PlayerStatsRepository playerStatsRepository;

    @Async
    @EventListener
    @Transactional
    public void handlePlayerStatsEvent(PlayerStatsEvent event) {
        PlayerStats stats = new PlayerStats();
        stats.setPlayerId(event.getPlayerId());
        stats.setGameDate(event.getGameDate());
        stats.setPoints(event.getPoints());
        stats.setRebounds(event.getRebounds());
        stats.setAssists(event.getAssists());
        stats.setSteals(event.getSteals());
        stats.setBlocks(event.getBlocks());
        stats.setFouls(event.getFouls());
        stats.setTurnovers(event.getTurnovers());
        stats.setMinutesPlayed(event.getMinutesPlayed());

        playerStatsRepository.save(stats);
    }
} 