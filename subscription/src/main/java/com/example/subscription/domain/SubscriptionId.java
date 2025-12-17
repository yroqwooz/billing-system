package com.example.subscription.domain;

import java.util.UUID;

public class SubscriptionId {
    public UUID getValue() {
        return value;
    }

    private final UUID value;

    public SubscriptionId(UUID value) {
        this.value = value;
    }
}
