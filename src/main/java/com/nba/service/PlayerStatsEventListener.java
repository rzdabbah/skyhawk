package com.nba.service;

import com.nba.event.PlayerStatsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerStatsEventListener {
    private final EventPublisherService eventPublisherService;

    @Async
    @EventListener
    public void handlePlayerStatsEvent(PlayerStatsEvent event) {
        try {
            // Set default team IDs if not provided
            if (event.getHomeTeamId() == null) {
                event.setHomeTeamId(1L); // Default home team ID
            }
            if (event.getAwayTeamId() == null) {
                event.setAwayTeamId(2L); // Default away team ID
            }
            
            log.info("Received player stats event, publishing to Kafka: {}", event);
            eventPublisherService.publishPlayerStatsEvent(event);
        } catch (Exception e) {
            log.error("Error publishing player stats event to Kafka: {}", e.getMessage(), e);
            throw e;
        }
    }
} 