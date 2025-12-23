package com.example.common;

import java.util.UUID;

public record UserId(UUID value) {
    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId fromString(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }
}