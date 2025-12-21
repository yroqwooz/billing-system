package com.example.subscription.web.dto;


import java.util.UUID;

public record CreateSubscriptionRequest(
        @NotNull UUID userId,
        @NotNull UUID planId
) {}