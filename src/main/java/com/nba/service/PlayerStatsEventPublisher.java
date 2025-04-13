package com.nba.service;

import com.nba.config.RabbitMQConfig;
import com.nba.event.PlayerStatsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerStatsEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishPlayerStatsEvent(PlayerStatsEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_PLAYER_STATS,
                RabbitMQConfig.ROUTING_KEY_PLAYER_STATS,
                event
            );
            log.info("Published player stats event for player: {}", event.getPlayerId());
        } catch (Exception e) {
            log.error("Failed to publish player stats event for player: {}", event.getPlayerId(), e);
            throw e;
        }
    }
} 