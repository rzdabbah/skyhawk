package com.nba.service;

import com.nba.domain.Game;
import com.nba.domain.Player;
import com.nba.domain.PlayerStats;
import com.nba.domain.Team;
import com.nba.event.PlayerStatsEvent;
import com.nba.repository.GameRepository;
import com.nba.repository.PlayerRepository;
import com.nba.repository.StatsRepository;
import com.nba.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerStatsKafkaConsumerTest {

    @Mock
    private StatsRepository playerStatsRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private PlayerStatsKafkaConsumer consumer;

    private PlayerStatsEvent testEvent;
    private Team testTeam;
    private Game testGame;
    private Player testPlayer;
    private Long teamId;

    @BeforeEach
    void setUp() {
        // Setup test event
        testEvent = new PlayerStatsEvent();
        testEvent.setPlayerId(1L);
        testEvent.setGameDate(LocalDateTime.now());
        testEvent.setHomeTeamId(1L);
        testEvent.setAwayTeamId(2L);
        testEvent.setPoints(25);
        testEvent.setRebounds(10);
        testEvent.setAssists(5);
        testEvent.setSteals(2);
        testEvent.setBlocks(1);
        testEvent.setFouls(3);
        testEvent.setTurnovers(2);
        testEvent.setMinutesPlayed(35.5f);

        // Setup test team
        testTeam = new Team();
        testTeam.setId(1L);
        testTeam.setName("Test Team");
        testTeam.setCity("Test City");
        teamId = 1L;

        // Setup test game
        testGame = new Game();
        testGame.setId(1L);
        testGame.setGameDate(LocalDateTime.now());
        testGame.setHomeTeamId(1L);
        testGame.setAwayTeamId(2L);

        // Setup test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("Test Player");
        testPlayer.setTeamId(1L);
    }

    @Test
    void consumePlayerStatsEvent_ShouldSavePlayerStats() {
        // Given
        when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
        when(gameRepository.findByGameDate(any())).thenReturn(Optional.of(testGame));
        when(playerRepository.findById(any())).thenReturn(Optional.of(testPlayer));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(1);

        // When
        consumer.consumePlayerStatsEvent(testEvent);

        // Then
        verify(playerStatsRepository, times(1)).save(any(PlayerStats.class));
    }

    @Test
    void consumePlayerStatsEvent_WhenTeamDoesNotExist_ShouldCreateTeam() {
        // Given
        when(teamRepository.findById(any())).thenReturn(Optional.empty());
        when(teamRepository.save(any())).thenReturn(testTeam);
        when(gameRepository.findByGameDate(any())).thenReturn(Optional.of(testGame));
        when(playerRepository.findById(any())).thenReturn(Optional.of(testPlayer));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(1);

        // When
        consumer.consumePlayerStatsEvent(testEvent);

        // Then
        verify(teamRepository, times(2)).save(any(Team.class));
    }

    @Test
    void consumePlayerStatsEvent_WhenGameDoesNotExist_ShouldCreateGame() {
        // Given
        when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
        when(gameRepository.findByGameDate(any())).thenReturn(Optional.empty());
        when(gameRepository.save(any())).thenReturn(testGame);
        when(playerRepository.findById(any())).thenReturn(Optional.of(testPlayer));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(1);

        // When
        consumer.consumePlayerStatsEvent(testEvent);

        // Then
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void consumePlayerStatsEvent_WhenPlayerDoesNotExist_ShouldCreatePlayer() {
        // Given
        when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
        when(gameRepository.findByGameDate(any())).thenReturn(Optional.of(testGame));
        when(playerRepository.findById(any())).thenReturn(Optional.empty());
        when(playerRepository.save(any())).thenReturn(testPlayer);
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(1);

        // When
        consumer.consumePlayerStatsEvent(testEvent);

        // Then
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void consumePlayerStatsEvent_WhenGameVerificationFails_ShouldNotSaveStats() {
        // Given
        when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
        when(gameRepository.findByGameDate(any())).thenReturn(Optional.of(testGame));
        when(playerRepository.findById(any())).thenReturn(Optional.of(testPlayer));
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any())).thenReturn(0);

        // When
        consumer.consumePlayerStatsEvent(testEvent);

        // Then
        verify(playerStatsRepository, never()).save(any(PlayerStats.class));
    }

   

    
} 