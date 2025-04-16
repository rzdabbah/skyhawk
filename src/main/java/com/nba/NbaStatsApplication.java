package com.nba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nba", "com.skyhawk.nba"})
@EnableScheduling
public class NbaStatsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NbaStatsApplication.class, args);
    }
} 