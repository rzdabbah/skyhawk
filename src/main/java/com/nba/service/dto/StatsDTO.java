package com.nba.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StatsDTO {
    @NotNull
    private Long playerId;
    @NotNull
    private Long gameId;

    @NotNull
    private Integer points;

    @NotNull
    @Min(0)
    private Integer rebounds;

    @NotNull
    @Min(0)
    private Integer assists;

    @NotNull
    @Min(0)
    private Integer steals;

    @NotNull
    @Min(0)
    private Integer blocks;

    @NotNull
    @Min(0)
    @Max(6)
    private Integer fouls;

    @NotNull
    @Min(0)
    private Integer turnovers;

    @NotNull
    @Min(0)
    @Max(48)
    private Float minutesPlayed;
} 