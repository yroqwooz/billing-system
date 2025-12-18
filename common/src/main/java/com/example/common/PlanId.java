package com.example.common;

import java.util.UUID;

public class PlanId {
    private final UUID id;

    public PlanId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

}