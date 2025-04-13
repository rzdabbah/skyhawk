package com.nba.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlayerStats {
    private Long id;
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