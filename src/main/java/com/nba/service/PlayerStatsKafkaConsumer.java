package com.nba.service;

import com.nba.domain.PlayerStats;
import com.nba.domain.Game;
import com.nba.domain.Team;
import com.nba.domain.Player;
import com.nba.event.PlayerStatsEvent;
import com.nba.repository.StatsRepository;
import com.nba.repository.GameRepository;
import com.nba.repository.TeamRepository;
import com.nba.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerStatsKafkaConsumer {
    private final StatsRepository playerStatsRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final JdbcTemplate jdbcTemplate;

    @KafkaListener(topics = "nba.player-stats", groupId = "nba-stats-group")
    @Transactional
    public void consumePlayerStatsEvent(PlayerStatsEvent event) {
        try {
            log.info("Received player stats event from Kafka: {}", event);
            
            // First ensure teams exist
            Team homeTeam = ensureTeamExists(event.getHomeTeamId(), "Home Team");
            Team awayTeam = ensureTeamExists(event.getAwayTeamId(), "Away Team");
            
            // Validate team IDs
            if (homeTeam.getId() == null || awayTeam.getId() == null) {
                log.error("Failed to get valid team IDs. Home team ID: {}, Away team ID: {}", 
                    homeTeam.getId(), awayTeam.getId());
                return;
            }
            
            log.info("Using team IDs - Home: {}, Away: {}", homeTeam.getId(), awayTeam.getId());
            
            // Then ensure we have a game record
            Game game = gameRepository.findByGameDate(event.getGameDate())
                .orElseGet(() -> {
                    log.info("Creating new game record for date: {} with teams - Home: {}, Away: {}", 
                        event.getGameDate(), homeTeam.getId(), awayTeam.getId());
                    Game newGame = new Game();
                    newGame.setGameDate(event.getGameDate());
                    newGame.setHomeTeamId(homeTeam.getId());
                    newGame.setAwayTeamId(awayTeam.getId());
                    newGame.setHomeTeamScore(event.getHomeTeamScore() != null ? event.getHomeTeamScore() : 0);
                    newGame.setAwayTeamScore(event.getAwayTeamScore() != null ? event.getAwayTeamScore() : 0);
                    return gameRepository.save(newGame);
                });
            
            log.info("Using game with ID: {} for player stats", game.getId());

            // Ensure player exists
            Player player = ensurePlayerExists(event.getPlayerId(), homeTeam.getId());
            
            // Verify game exists in database
            boolean gameExists = verifyGameExists(game.getId());
            if (!gameExists) {
                log.error("Game with ID {} does not exist in database, cannot save player stats", game.getId());
                return;
            }
            
            PlayerStats stats = new PlayerStats();
            stats.setPlayerId(player.getId());
            stats.setGameId(game.getId());
            stats.setPoints(event.getPoints());
            stats.setRebounds(event.getRebounds());
            stats.setAssists(event.getAssists());
            stats.setSteals(event.getSteals());
            stats.setBlocks(event.getBlocks());
            stats.setFouls(event.getFouls());
            stats.setTurnovers(event.getTurnovers());
            stats.setMinutesPlayed(event.getMinutesPlayed());

            playerStatsRepository.save(stats);
            log.info("Successfully saved player stats for player: {} in game: {}", player.getId(), game.getId());
        } catch (Exception e) {
            log.error("Error processing player stats event from Kafka: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private boolean verifyGameExists(Long gameId) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM games WHERE id = ?", 
                Integer.class, 
                gameId
            );
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("Error verifying game existence: {}", e.getMessage(), e);
            return false;
        }
    }
    
    private Team ensureTeamExists(Long teamId, String teamName) {
        if (teamId == null) {
            log.warn("Team ID is null, creating a new team with name: {}", teamName);
            Team newTeam = new Team();
            newTeam.setName(teamName);
            newTeam.setCity("Unknown");
            Team savedTeam = teamRepository.save(newTeam);
            if (savedTeam.getId() == null) {
                throw new IllegalStateException("Failed to save team: " + teamName);
            }
            log.info("Created new team with ID: {}", savedTeam.getId());
            return savedTeam;
        }
        
        return teamRepository.findById(teamId)
            .orElseGet(() -> {
                log.warn("Team with ID {} not found, creating a new team", teamId);
                Team newTeam = new Team();
                newTeam.setName(teamName);
                newTeam.setCity("Unknown");
                Team savedTeam = teamRepository.save(newTeam);
                if (savedTeam.getId() == null) {
                    throw new IllegalStateException("Failed to save team: " + teamName);
                }
                log.info("Created new team with ID: {}", savedTeam.getId());
                return savedTeam;
            });
    }
    
    private Player ensurePlayerExists(Long playerId, Long teamId) {
        if (playerId == null) {
            log.warn("Player ID is null, creating a new player");
            Player newPlayer = new Player();
            newPlayer.setName("Unknown Player");
            newPlayer.setTeamId(teamId);
            return playerRepository.save(newPlayer);
        }
        
        return playerRepository.findById(playerId)
            .orElseGet(() -> {
                log.warn("Player with ID {} not found, creating a new player", playerId);
                Player newPlayer = new Player();
                newPlayer.setName("Unknown Player");
                newPlayer.setTeamId(teamId);
                return playerRepository.save(newPlayer);
            });
    }
} 