package com.nba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nba", "com.skyhawk.nba"})
public class NbaStatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NbaStatsApplication.class, args);
    }
} 