package com.example.common;

public record PlanSnapshot(
        PlanId id,
        PlanDuration duration,
        Money price,
        PlanStatus status
) {}
