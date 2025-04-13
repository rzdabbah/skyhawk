package com.nba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StatsConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatsConsumerApplication.class, args);
    }
} 