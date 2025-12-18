package com.example.subscription.domain.id;

import java.util.UUID;

public final class PlanId {
    private final UUID value;

    public PlanId(UUID id) {
        this.value = id;
    }

    public UUID getId() {
        return value;
    }
}
