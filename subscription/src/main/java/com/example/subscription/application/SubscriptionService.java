package com.example.subscription.application;

import com.example.subscription.application.exceptions.PlanNotFoundException;
import com.example.subscription.application.exceptions.SubscriptionNotFoundException;
import com.example.subscription.application.port.PlanQuery;
import com.example.subscription.application.port.PlanSnapshot;
import com.example.subscription.domain.id.PlanId;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.id.UserId;
import com.example.subscription.domain.model.SubscriptionPeriod;

import com.example.subscription.infrastructure.SubscriptionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final PlanQuery planQuery;

    public SubscriptionService(
            SubscriptionRepository repository,
            PlanQuery planQuery
    ) {
        this.repository = repository;
        this.planQuery = planQuery;
    }

    public SubscriptionId createSubscription(UserId userId, PlanId planId) {
        PlanSnapshot plan = planQuery.getPlan(planId)
                .orElseThrow(PlanNotFoundException::new);

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(plan.durationDays());

        Subscription subscription = Subscription.create(
                SubscriptionId.newId(),
                userId,
                planId,
                SubscriptionPeriod.of(start, end)
        );

        subscription.activate();
        repository.save(subscription);

        return subscription.getId();
    }

    public void cancelSubscription(SubscriptionId id) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(SubscriptionNotFoundException::new);

        subscription.cancel();
        repository.save(subscription);
    }
}
