
package com.example.subscription.web.dto;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public record CreateSubscriptionRequest(
        @NotNull UUID userId,
        @NotNull UUID planId
) {}