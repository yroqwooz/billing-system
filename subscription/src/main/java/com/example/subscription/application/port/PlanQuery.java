package com.example.subscription.application.port;

import com.example.subscription.domain.id.PlanId;

import java.util.Optional;

public interface PlanQuery {

    Optional<PlanSnapshot> getPlan(PlanId planId);
}
