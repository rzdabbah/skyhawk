package com.nba.service;

import com.nba.event.GameStatsEvent;

public interface GameStatsService {
    GameStatsEvent publishGameStatsEvent(GameStatsEvent event);
    GameStatsEvent calculateAndPublishGameStats(Long gameId);
} 