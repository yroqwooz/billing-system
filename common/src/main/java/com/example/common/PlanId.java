package com.example.common;

import java.util.UUID;

public record PlanId(UUID value) {
    public static PlanId newId() {
        return new PlanId(UUID.randomUUID());
    }

    public static PlanId fromString(String uuid) {
        return new PlanId(UUID.fromString(uuid));
    }
}