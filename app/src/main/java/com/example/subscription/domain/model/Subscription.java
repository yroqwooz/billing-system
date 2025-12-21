package com.example.subscription.domain.model;

import com.example.subscription.domain.exceptions.IllegalSubscriptionStateTransition;
import com.example.subscription.domain.id.PlanId;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.id.UserId;

import java.time.LocalDate;

public class Subscription {

    private final SubscriptionId id;
    private final UserId userId;
    private final PlanId planId;

    public long version;
    private SubscriptionStatus status;
    private SubscriptionPeriod period;

    private Subscription(
            SubscriptionId id,
            UserId userId,
            PlanId planId,
            SubscriptionPeriod period,
            SubscriptionStatus status,
            long version
    ) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.period = period;
        this.status = status;
        this.version = version;

    }

    public static Subscription create(
            SubscriptionId id,
            UserId userId,
            PlanId planId,
            SubscriptionPeriod period
    ) {
        return new Subscription(
                id,
                userId,
                planId,
                period,
                SubscriptionStatus.CREATED,
                0L
        );
    }

    public void activate() {
        assertTransition(SubscriptionStatus.ACTIVE);
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void cancel() {
        if (status == SubscriptionStatus.CANCELLED) {
            return;
        }
        assertTransition(SubscriptionStatus.CANCELLED);
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void expire(LocalDate today) {
        if (status == SubscriptionStatus.ACTIVE && period.endsBefore(today)) {
            this.status = SubscriptionStatus.EXPIRED;
        }
    }

    private void assertTransition(SubscriptionStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new IllegalSubscriptionStateTransition(status, target);
        }
    }

    public static Subscription restore(
            SubscriptionId id,
            UserId userId,
            PlanId planId,
            SubscriptionStatus status,
            SubscriptionPeriod period,
            long version
    ) {
        return new Subscription(id, userId, planId, period, status, version);
    }

    public long version() {
        return version;
    }

    public SubscriptionId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public PlanId getPlanId() {
        return planId;
    }

    public SubscriptionPeriod getPeriod() {
        return period;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }
}
