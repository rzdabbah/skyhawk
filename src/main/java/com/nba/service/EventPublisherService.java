package com.nba.service;

import com.nba.event.PlayerStatsEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final KafkaTemplate<String, PlayerStatsEvent> kafkaTemplate;

    public void publishPlayerStatsEvent(PlayerStatsEvent event) {
        String topic = "nba.player-stats";
        String key = "player-" + event.getPlayerId() + "-game-" + event.getGameDate();
        
        CompletableFuture<SendResult<String, PlayerStatsEvent>> future = 
                kafkaTemplate.send(topic, key, event);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Published player stats event to Kafka: {} with offset: {}", 
                        key, result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish player stats event to Kafka: {}", key, ex);
            }
        });
    }
} 