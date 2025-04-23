package com.nba.service.impl;

import com.nba.domain.Game;
import com.nba.event.GameStatsEvent;
import com.nba.repository.GameRepository;
import com.nba.repository.PlayerStatsRepository;
import com.nba.service.GameStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameStatsServiceImpl implements GameStatsService {
    private final KafkaTemplate<String, GameStatsEvent> kafkaTemplate;
    private final GameRepository gameRepository;
    private final PlayerStatsRepository playerStatsRepository;

    @Override
    public GameStatsEvent publishGameStatsEvent(GameStatsEvent event) {
        String topic = "nba.game-stats";
        String key = "game-" + event.getGameId();
        
        kafkaTemplate.send(topic, key, event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Published game stats event to Kafka: {} with offset: {}", 
                            key, result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish game stats event to Kafka: {}", key, ex);
                }
            });
        
        return event;
    }

    @Override
    public GameStatsEvent calculateAndPublishGameStats(Long gameId) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
        
        // Calculate game statistics from player stats
        GameStatsEvent event = new GameStatsEvent();
        event.setGameId(gameId);
        event.setGameDate(game.getGameDate());
        event.setHomeTeamId(game.getHomeTeamId());
        event.setAwayTeamId(game.getAwayTeamId());
        event.setHomeTeamScore(game.getHomeTeamScore());
        event.setAwayTeamScore(game.getAwayTeamScore());
        
        // Get aggregated statistics from player stats
        var stats = playerStatsRepository.calculateGameAverages(gameId);
        event.setTotalPoints(stats.get("points").intValue());
        event.setTotalRebounds(stats.get("rebounds").intValue());
        event.setTotalAssists(stats.get("assists").intValue());
        event.setTotalSteals(stats.get("steals").intValue());
        event.setTotalBlocks(stats.get("blocks").intValue());
        event.setTotalFouls(stats.get("fouls").intValue());
        event.setTotalTurnovers(stats.get("turnovers").intValue());
        event.setAverageMinutesPlayed(stats.get("minutes_played").floatValue());
        
        return publishGameStatsEvent(event);
    }
} 