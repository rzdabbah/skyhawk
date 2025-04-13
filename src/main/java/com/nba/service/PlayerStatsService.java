package com.nba.service;

import com.nba.service.dto.PlayerStatsDTO;
import java.util.Map;

public interface PlayerStatsService {
    void logPlayerStats(PlayerStatsDTO stats);
    Map<String, Double> getPlayerSeasonAverages(Long playerId);
    Map<String, Double> getTeamSeasonAverages(Long teamId);
} 