package com.nba.service.impl;

import com.nba.domain.Player;
import com.nba.event.GameStatsEvent;
import com.nba.event.PlayerStatsEvent;
import com.nba.repository.PlayerRepository;
import com.nba.repository.StatsRepository;
import com.nba.service.StatsService;
import com.nba.service.StatsEventPublisher;
import com.nba.service.dto.StatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.time.LocalDateTime;
import com.nba.service.dto.PlayerAverageDTO;
import com.nba.service.dto.TeamAverageDTO;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.Optional;
import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final PlayerRepository playerRepository;
    private final StatsRepository statsRepository;
    private final StatsEventPublisher eventPublisher;
    private final KafkaTemplate<String, GameStatsEvent> kafkaTemplate;


    @Override
    @Transactional
    @CacheEvict(value = {"playerAverages", "teamAverages"}, allEntries = true)
    public void logPlayerStats(StatsDTO stats) throws ResponseStatusException{
        try{
            this.validateStatDTO(stats);
        }
        catch(Exception e){
            Logger.getLogger(this.getClass().getName()).info("Validation Error" + e.getLocalizedMessage());
            throw e;
        }
       
        // Verify player exists
        Player player = playerRepository.findById(stats.getPlayerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));

        // Create and publish event
        PlayerStatsEvent event = new PlayerStatsEvent();
        event.setPlayerId(player.getId());
        event.setGameId(stats.getGameId());
        event.setGameDate(LocalDateTime.now());
        event.setPoints(stats.getPoints());
        event.setRebounds(stats.getRebounds());
        event.setAssists(stats.getAssists());
        event.setSteals(stats.getSteals());
        event.setBlocks(stats.getBlocks());
        event.setFouls(stats.getFouls());
        event.setTurnovers(stats.getTurnovers());
        event.setMinutesPlayed(stats.getMinutesPlayed());

        eventPublisher.publishPlayerStatsEvent(event);
    }

    @Override
    @Cacheable(value = "getPlayersSeasonAverages")
    public Optional<List<PlayerAverageDTO>> getPlayersSeasonAverages() throws Exception {
        return statsRepository.getPlayersSeasonAverages();
    }

    @Override
    @Cacheable(value = "getTeamSeasonAverages")
    public Optional<List<TeamAverageDTO>> getTeamsSeasonAverages()  throws Exception {
        return statsRepository.getTeamsSeasonAverages();
    }

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
private void validateStatDTO(StatsDTO  dto) throws ResponseStatusException{
    if (dto.getFouls() < 0 || dto.getFouls() > 6) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Fouls Value must be in 0..6");
    if (dto.getTurnovers() < 0 || dto.getTurnovers() > 48) throw new  ResponseStatusException(HttpStatus.BAD_REQUEST,"Turnovers Value must be in 0..48");
    if (dto.getMinutesPlayed() < 0 || dto.getMinutesPlayed() > 48) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"MinutesPlayed Value must be in 0..48");
}

} 