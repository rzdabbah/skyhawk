package com.nba.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Team {
    private Long id;
    private String name;
    private String city;
} 