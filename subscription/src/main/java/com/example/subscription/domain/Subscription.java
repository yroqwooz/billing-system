package com.example.subscription.domain;


import java.util.Objects;
import java.util.UUID;

public final class Subscription {
    private final UserId userId;
    private final PlanId planId;
    private final SubscriptionId subscriptionId;
    private final SubscriptionPeriod period;
    private SubscriptionStatus status;


    public Subscription(UserId userId, PlanId planId, SubscriptionPeriod period) {
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.planId = Objects.requireNonNull(planId, "planId must not be null");
        this.period = Objects.requireNonNull(period, "period must be not null");
        this.status = SubscriptionStatus.NEW;
        subscriptionId = new SubscriptionId(UUID.randomUUID());
    }

    public UserId getUserId() {
        return userId;
    }

    public PlanId getPlanId() {
        return planId;
    }

    public SubscriptionId getSubscriptionId() {
        return subscriptionId;
    }

    public SubscriptionPeriod getPeriod() {
        return period;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

}
