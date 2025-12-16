package com.example.subscription.domain;


public class Subscription {
    private final UserId userId;
    private final PlanId planId;
    private final SubscriptionPeriod period;
    private final SubscriptionStatus status;


    public Subscription(UserId userId, SubscriptionPeriod period, PlanId planId, SubscriptionStatus status) {
        this.userId = userId;
        this.planId = planId;
        this.period = period;
        this.status = status;
    }
}
