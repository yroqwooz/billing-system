package com.example.subscription.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TestConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
    @Bean
    public com.example.subscription.application.port.PlanQuery planQuery() {
        return planId -> java.util.Optional.of(new com.example.subscription.application.port.PlanSnapshot(planId, 30));
    }
}
