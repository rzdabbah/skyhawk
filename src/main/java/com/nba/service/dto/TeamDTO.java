package com.nba.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamDTO {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "City is required")
    private String city;
} 