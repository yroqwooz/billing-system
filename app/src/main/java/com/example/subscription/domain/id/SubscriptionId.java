package com.example.subscription.domain.id;

import java.util.UUID;

public record SubscriptionId(UUID value) {

    public static SubscriptionId newId() {
        return new SubscriptionId(UUID.randomUUID());
    }
}

