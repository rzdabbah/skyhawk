package com.nba.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Game {
    private Long id;
    private LocalDateTime gameDate;
    private Long homeTeamId;
    private Long awayTeamId;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
} 