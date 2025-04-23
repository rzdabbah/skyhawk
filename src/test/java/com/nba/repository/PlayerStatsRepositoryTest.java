package com.nba.repository;

import com.nba.domain.PlayerStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = com.nba.NbaStatsApplication.class)
@ActiveProfiles("test")
@Transactional
class PlayerStatsRepositoryTest {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PlayerStats testStats;
    private Long teamId;
    private Long playerId;
    private Long gameId;

    @BeforeEach
    void setUp() {
        // Create test team
        teamId = createTestTeam();
        
        // Create test player
        playerId = createTestPlayer();
        
        // Create test game
        gameId = createTestGame();

        // Create test stats
        testStats = new PlayerStats();
        testStats.setPlayerId(playerId);
        testStats.setGameId(gameId);
        testStats.setPoints(25);
        testStats.setRebounds(10);
        testStats.setAssists(5);
        testStats.setSteals(2);
        testStats.setBlocks(1);
        testStats.setFouls(3);
        testStats.setTurnovers(2);
        testStats.setMinutesPlayed(35.5f);
    }

    private Long createTestTeam() {
        // INSERT INTO teams (name, city, conference, division, founded_year) VALUES

        Random rand = new Random();

        jdbcTemplate.update("INSERT INTO teams (name, city) VALUES ( ?, ?) ;", "Test Team"+rand.nextInt(50), "dier el assad"+rand.nextInt(50));
        return jdbcTemplate.queryForObject("SELECT max(id) from teams", Long.class);
    }

    private Long createTestPlayer() {
        //INSERT INTO players (name, team_id, jersey_number) VALUES

        jdbcTemplate.update("INSERT INTO players (name,  team_id, jersey_number) VALUES (?, ?, ?);", "rami",  teamId, +new Random().nextInt(10));
        return jdbcTemplate.queryForObject("SELECT max(id) from players", Long.class);
    }

    private Long createTestGame() {
        // INSERT INTO games (game_date, home_team_id, away_team_id, home_team_score, away_team_score) VALUES


        Long awayTeamId = createTestTeam();
        jdbcTemplate.update("INSERT INTO games ( game_date, home_team_id, away_team_id , home_team_score, away_team_score) VALUES ( ?, ?, ?, ?, ?);", LocalDate.now(), teamId, awayTeamId, new Random().nextInt(100), new Random().nextInt(100));
        return jdbcTemplate.queryForObject("SELECT max(id) from games", Long.class);
    }

    @Test
    void save_ShouldCreateNewPlayerStats() {
        // When
        PlayerStats savedStats = playerStatsRepository.save(testStats);

        // Then
        assertThat(savedStats.getId()).isNotNull();
        assertThat(savedStats.getPlayerId()).isEqualTo(playerId);
        assertThat(savedStats.getGameId()).isEqualTo(gameId);
        assertThat(savedStats.getPoints()).isEqualTo(25);
    }

    @Test
    void findById_ShouldReturnPlayerStats() {
        // Given
        PlayerStats savedStats = playerStatsRepository.save(testStats);

        // When
        Optional<PlayerStats> foundStats = playerStatsRepository.findById(savedStats.getId());

        // Then
        assertThat(foundStats).isPresent();
        assertThat(foundStats.get().getPoints()).isEqualTo(25);
        assertThat(foundStats.get().getRebounds()).isEqualTo(10);
    }

    @Test
    void findByPlayerId_ShouldReturnPlayerStatsList() {
        // Given
        playerStatsRepository.save(testStats);

        // When
        List<PlayerStats> statsList = playerStatsRepository.findByPlayerId(playerId);

        // Then
        assertThat(statsList).isNotEmpty();
        assertThat(statsList.get(0).getPlayerId()).isEqualTo(playerId);
    }

    @Test
    void calculatePlayerAverages_ShouldReturnAverages() {
        // Given
        playerStatsRepository.save(testStats);

        // When
        Map<String, Double> averages = playerStatsRepository.calculatePlayerAverages(playerId);

        // Then
        assertThat(averages).isNotEmpty();
        assertThat(averages.get("POINTS")).isEqualTo(25.0);
        assertThat(averages.get("REBOUNDS")).isEqualTo(10.0);
        assertThat(averages.get("ASSISTS")).isEqualTo(5.0);
    }

    @Test
    void calculateTeamAverages_ShouldReturnAverages() {
        // Given
        playerStatsRepository.save(testStats);

        // When
        Map<String, Double> averages = playerStatsRepository.calculateTeamAverages(teamId);

        // Then
        assertThat(averages).isNotEmpty();
        assertThat(averages.get("POINTS")).isEqualTo(25.0);
        assertThat(averages.get("REBOUNDS")).isEqualTo(10.0);
    }

    @Test
    void calculateGameAverages_ShouldReturnAverages() {
        // Given
        playerStatsRepository.save(testStats);

        // When
        Map<String, Double> averages = playerStatsRepository.calculateGameAverages(gameId);

        // Then
        assertThat(averages).isNotEmpty();
        assertThat(averages.get("POINTS")).isEqualTo(25.0);
        assertThat(averages.get("REBOUNDS")).isEqualTo(10.0);
    }
    
} 