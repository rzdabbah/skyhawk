package com.nba.service;

import com.nba.config.RabbitMQConfig;
import com.nba.domain.PlayerStats;
import com.nba.event.PlayerStatsEvent;
import com.nba.repository.PlayerStatsRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsConsumerService {
    private final PlayerStatsRepository playerStatsRepository;
    private final MeterRegistry meterRegistry;
    private static final int BATCH_SIZE = 100;
    private final List<PlayerStats> statsBuffer = new ArrayList<>();

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PLAYER_STATS)
    @Retryable(
        value = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @Transactional
    public void processPlayerStats(PlayerStatsEvent event) {
        long startTime = System.nanoTime();
        try {
            log.info("Processing player stats for player: {}", event.getPlayerId());
            
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

            statsBuffer.add(stats);
            
            if (statsBuffer.size() >= BATCH_SIZE) {
                saveBatch();
            }

            long processingTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            meterRegistry.timer("stats.processing.time").record(processingTime, TimeUnit.MILLISECONDS);
            meterRegistry.counter("stats.processed").increment();
            
            log.info("Successfully processed stats for player: {} in {}ms", event.getPlayerId(), processingTime);
        } catch (Exception e) {
            meterRegistry.counter("stats.processing.errors").increment();
            log.error("Failed to process stats for player: {}", event.getPlayerId(), e);
            throw e;
        }
    }

    @Transactional
    protected void saveBatch() {
        if (!statsBuffer.isEmpty()) {
            playerStatsRepository.saveAll(statsBuffer);
            statsBuffer.clear();
            log.info("Saved batch of {} stats", BATCH_SIZE);
        }
    }
} 