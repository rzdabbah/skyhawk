package com.nba.event;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlayerStatsEvent {
    private Long playerId;
    private LocalDateTime gameDate;
    private Integer points;
    private Integer rebounds;
    private Integer assists;
    private Integer steals;
    private Integer blocks;
    private Integer fouls;
    private Integer turnovers;
    private Float minutesPlayed;
} 