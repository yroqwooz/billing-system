package com.example.subscription.application.port;


import com.example.subscription.domain.id.PlanId;

public record PlanSnapshot(
        PlanId id,
        int durationDays
) {}
