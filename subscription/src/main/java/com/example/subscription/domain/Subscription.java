package com.example.subscription.domain;


public class Subscription {
    private final UserId userId;
    private final PlanId planId;
    private final SubscriptionId subscriptionId;
    private final SubscriptionPeriod period;
    private final SubscriptionStatus status;


    public Subscription(UserId userId, PlanId planId, SubscriptionId subscriptionId, SubscriptionPeriod period, SubscriptionStatus status) {
        this.userId = userId;
        this.planId = planId;
        this.subscriptionId = subscriptionId;
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
