package com.example.app.config;

import com.example.billing.infrastructure.repository.PlanRepository;
import com.example.subscription.application.port.PlanQuery;
import com.example.subscription.application.port.PlanSnapshot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Period;

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
    @Profile("!test")
    public PlanQuery planQuery(PlanRepository planRepository) {
        return planId -> planRepository.findById(planId)
                .map(plan -> new PlanSnapshot(
                        planId,
                        plan.getDuration().asPeriod()
                ));
    }
}