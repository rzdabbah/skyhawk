package com.nba.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Player {
    private Long id;
    private String firstName;
    private String lastName;
    private Long teamId;
    private Integer jerseyNumber;
} 