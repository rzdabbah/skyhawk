package com.nba.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {
    private Long id;
    private String name;
    private Long teamId;
    private Integer jerseyNumber;
} 