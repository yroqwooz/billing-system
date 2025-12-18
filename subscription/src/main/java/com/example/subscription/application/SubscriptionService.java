package com.example.subscription.application;

import com.example.subscription.domain.PlanId;
import com.example.subscription.domain.Subscription;
import com.example.subscription.domain.UserId;
import com.example.subscription.infrastructure.SubscriptionRepository;

import java.util.Objects;

public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanQuery planQuery; // <- интерфейс из common

    public SubscriptionService(SubscriptionRepository sRepo, PlanQuery planQuery) {
        this.subscriptionRepository = sRepo;
        this.planQuery = planQuery;
    }

    public Subscription createSubscription(UserId userId, PlanId planId) {
        PlanSnapshot plan = planQuery.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        if (plan.getStatus() != PlanStatus.ACTIVE) {
            throw new IllegalStateException("Plan not active");
        }

        LocalDate start = LocalDate.now();
        LocalDate end = plan.getDuration().addTo(start);
        SubscriptionPeriod period = new SubscriptionPeriod(start, end);

        Subscription subscription = new Subscription(userId, planId, period);
        subscriptionRepository.save(subscription);
        return subscription;
    }
}
