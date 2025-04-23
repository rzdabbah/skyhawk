package com.nba.service;

import com.nba.event.PlayerStatsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerStatsEventListenerTest {

    @Mock
    private EventPublisherService eventPublisherService;

    @InjectMocks
    private PlayerStatsEventListener eventListener;

    private PlayerStatsEvent testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new PlayerStatsEvent();
        testEvent.setPlayerId(1L);
        testEvent.setGameDate(LocalDateTime.now());
        testEvent.setPoints(25);
        testEvent.setRebounds(10);
        testEvent.setAssists(5);
        testEvent.setSteals(2);
        testEvent.setBlocks(1);
        testEvent.setFouls(3);
        testEvent.setTurnovers(2);
        testEvent.setMinutesPlayed(35.5f);
    }

    @Test
    void handlePlayerStatsEvent_ShouldPublishEvent() {
        // When
        eventListener.handlePlayerStatsEvent(testEvent);

        // Then
        verify(eventPublisherService, times(1)).publishPlayerStatsEvent(any(PlayerStatsEvent.class));
    }

    @Test
    void handlePlayerStatsEvent_WithNullTeamIds_ShouldSetDefaultTeamIds() {
        // Given
        testEvent.setHomeTeamId(null);
        testEvent.setAwayTeamId(null);

        // When
        eventListener.handlePlayerStatsEvent(testEvent);

        // Then
        verify(eventPublisherService, times(1)).publishPlayerStatsEvent(argThat(event -> 
            event.getHomeTeamId() != null && 
            event.getAwayTeamId() != null
        ));
    }

    @Test
    void handlePlayerStatsEvent_WhenPublisherThrowsException_ShouldPropagateException() {
        // Given
        doThrow(new RuntimeException("Test exception"))
            .when(eventPublisherService)
            .publishPlayerStatsEvent(any(PlayerStatsEvent.class));

        // When/Then
        org.junit.jupiter.api.Assertions.assertThrows(
            RuntimeException.class,
            () -> eventListener.handlePlayerStatsEvent(testEvent)
        );
    }
} 