package com.example.subscription.application;

import com.example.subscription.application.exceptions.PlanNotFoundException;
import com.example.subscription.application.exceptions.SubscriptionNotFoundException;
import com.example.subscription.application.port.PlanQuery;
import com.example.subscription.application.port.PlanSnapshot;
import com.example.common.PlanId;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.model.Subscription;
import com.example.common.UserId;
import com.example.subscription.domain.model.SubscriptionPeriod;

import com.example.subscription.infrastructure.SubscriptionRepository;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Transactional
@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final PlanQuery planQuery;
    private final Clock clock;

    public SubscriptionService(
            SubscriptionRepository repository,
            PlanQuery planQuery,
            Clock clock
    ) {
        this.repository = repository;
        this.planQuery = planQuery;
        this.clock = clock;
    }

    public SubscriptionId createSubscription(UserId userId, PlanId planId) {
        PlanSnapshot plan = planQuery.getPlan(planId)
                .orElseThrow(PlanNotFoundException::new);

        LocalDate start = LocalDate.now(clock);
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
    @Transactional
    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public void cancelSubscription(SubscriptionId id) {
        Subscription subscription = repository.findById(id)
                .orElseThrow(SubscriptionNotFoundException::new);

        subscription.cancel();
        repository.save(subscription);
    }
}
