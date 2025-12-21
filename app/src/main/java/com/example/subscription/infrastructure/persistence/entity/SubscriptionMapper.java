package com.example.subscription.infrastructure.persistence.entity;

import com.example.subscription.domain.id.PlanId;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.id.UserId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.model.SubscriptionPeriod;
import com.example.subscription.domain.model.SubscriptionStatus;

public final class SubscriptionMapper {

    private SubscriptionMapper() {}

    public static SubscriptionEntity toEntity(Subscription subscription) {
        SubscriptionEntity e = new SubscriptionEntity();
        e.setId(subscription.getId().value());
        updateEntity(e, subscription);
        return e;
    }

    public static void updateEntity(
            SubscriptionEntity e,
            Subscription subscription
    ) {
        e.setUserId(subscription.getUserId().value());
        e.setPlanId(subscription.getPlanId().value());
        e.setStatus(
                SubscriptionStatusJpa.valueOf(subscription.getStatus().name())
        );
        e.setStartDate(subscription.getPeriod().startDate());
        e.setEndDate(subscription.getPeriod().endDate());
    }


    public static Subscription toDomain(SubscriptionEntity e) {
        return Subscription.restore(
                new SubscriptionId(e.getId()),
                new UserId(e.getUserId()),
                new PlanId(e.getPlanId()),
                SubscriptionStatus.valueOf(e.getStatus().name()),
                SubscriptionPeriod.of(e.getStartDate(), e.getEndDate()),
                e.getVersion()
        );
    }
}