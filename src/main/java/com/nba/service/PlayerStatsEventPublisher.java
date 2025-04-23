package com.nba.service;

import com.nba.event.PlayerStatsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerStatsEventPublisher {
    private final KafkaTemplate<String, PlayerStatsEvent> kafkaTemplate;
    private static final String TOPIC = "player.stats.topic";

    public void publishPlayerStatsEvent(PlayerStatsEvent event) {
        try {
            kafkaTemplate.send(TOPIC, "player-" + event.getPlayerId(), event);
            log.info("Published player stats event for player: {}", event.getPlayerId());
        } catch (Exception e) {
            log.error("Failed to publish player stats event for player: {}", event.getPlayerId(), e);
            throw e;
        }
    }
} 