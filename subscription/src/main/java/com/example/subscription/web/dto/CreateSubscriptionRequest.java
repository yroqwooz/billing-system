package com.example.subscription.web.dto;

import java.util.UUID;

public record CreateSubscriptionRequest(
        UUID userId,
        UUID planId
) {}