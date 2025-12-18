package com.example.subscription.domain.id;

import java.util.UUID;

public final class UserId {
    private final UUID value;

    public UserId(UUID id) {
        this.value = id;
    }

    public UUID getValue() {
        return value;
    }
}
