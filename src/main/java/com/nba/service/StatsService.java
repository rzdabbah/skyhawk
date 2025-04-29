package com.nba.service;

import com.nba.service.dto.StatsDTO;
import com.nba.event.GameStatsEvent;
import java.util.List;
import com.nba.service.dto.*;
import java.util.Optional;
public interface StatsService {
    void logPlayerStats(StatsDTO stats);
    Optional<List<PlayerAverageDTO>> getPlayersSeasonAverages()  throws Exception ;
    Optional<List<TeamAverageDTO>> getTeamsSeasonAverages()  throws Exception ;
    GameStatsEvent publishGameStatsEvent(GameStatsEvent event);

} 