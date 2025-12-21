package com.example.subscription.web.dto;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;

public record CreateSubscriptionRequest(
        @NotNull UUID userId,
        @NotNull UUID planId
) {}