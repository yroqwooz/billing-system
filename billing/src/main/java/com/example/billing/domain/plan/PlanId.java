package com.example.billing.domain.plan;

import java.util.UUID;

public record PlanId(UUID value) {

    public static PlanId newId() {
        return new PlanId(UUID.randomUUID());
    }
}