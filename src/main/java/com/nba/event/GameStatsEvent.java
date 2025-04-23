package com.nba.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GameStatsEvent {
    private Long gameId;
    private Long homeTeamId;
    private Long awayTeamId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime gameDate;
    
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Integer totalPoints;
    private Integer totalRebounds;
    private Integer totalAssists;
    private Integer totalSteals;
    private Integer totalBlocks;
    private Integer totalFouls;
    private Integer totalTurnovers;
    private Float averageMinutesPlayed;
} 