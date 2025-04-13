package com.nba.service;

import com.nba.domain.Team;
import com.nba.service.dto.TeamDTO;

import java.util.List;

public interface TeamService {
    Team createTeam(TeamDTO teamDTO);
    Team getTeam(Long id);
    List<Team> getAllTeams();
} 