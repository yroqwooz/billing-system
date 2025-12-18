package com.example.subscription.application;

import com.example.common.PlanSnapshot;
import com.example.subscription.domain.id.PlanId;
import com.example.subscription.domain.Subscription;
import com.example.subscription.domain.id.UserId;
import com.example.subscription.infrastructure.SubscriptionRepository;

public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanQuery planQuery; // <- интерфейс из common

    public SubscriptionService(SubscriptionRepository sRepo, PlanQuery planQuery) {
        this.subscriptionRepository = sRepo;
        this.planQuery = planQuery;
    }

    public Subscription createSubscription(UserId userId, PlanId planId) {

        PlanSnapshot plan = planQuery.findById(planId);

        if (plan.status() != ACTIVE) {
            throw new IllegalStateException("Plan is not active");
        }

        Subscription subscription = Subscription.create(
                userId,
                plan.id(),
                SubscriptionPeriod.from(plan.duration())
        );

        subscription.activate();

        subscriptionRepository.save(subscription);

        return subscription;
    }
}
