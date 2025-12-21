package com.example.subscription.application.port;


import com.example.common.PlanId;

public record PlanSnapshot(
        PlanId id,
        int durationDays
) {}
