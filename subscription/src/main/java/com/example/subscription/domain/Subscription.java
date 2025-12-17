package com.example.subscription.domain;


public class Subscription {
    private final UserId userId;
    private final PlanId planId;
    private final SubscriptionId subscriptionId;
    private final SubscriptionPeriod period;
    private final SubscriptionStatus status;


    public Subscription(UserId userId, SubscriptionPeriod period, PlanId planId, SubscriptionStatus status) {
        this.userId = userId;
        this.planId = planId;
        this.period = period;
        this.status = status;
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
