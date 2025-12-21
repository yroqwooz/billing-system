package com.example.subscription.application.port;

import com.example.subscription.domain.id.PlanId;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class StubPlanQuery implements PlanQuery {

    @Override
    public Optional<PlanSnapshot> getPlan(PlanId planId) {
        return Optional.of(new PlanSnapshot(
                planId,
                30 // days
        ));
    }
}