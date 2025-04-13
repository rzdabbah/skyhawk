package com.nba.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerDTO {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Team ID is required")
    private Long teamId;
    
    private Integer jerseyNumber;
} 