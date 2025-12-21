package com.example.app.config;

import com.example.subscription.application.port.PlanQuery;
import com.example.subscription.application.port.PlanSnapshot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Configuration
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    /**
     * Stub implementation for development/testing
     * Replace with real billing integration in production
     */
    @Bean
    @Profile("!prod")
    public PlanQuery stubPlanQuery() {
        return planId -> java.util.Optional.of(
                new PlanSnapshot(planId, 30)
        );
    }
}