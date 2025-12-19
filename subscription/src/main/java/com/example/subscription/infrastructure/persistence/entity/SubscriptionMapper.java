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
        e.setUserId(subscription.getUserId().value());
        e.setPlanId(subscription.getPlanId().value());
        e.setStatus(subscription.getStatus().name());
        e.setStartDate(subscription.getPeriod().start());
        e.setEndDate(subscription.getPeriod().end());
        return e;
    }

    public static Subscription toDomain(SubscriptionEntity e) {
        return Subscription.restore(
                new SubscriptionId(e.getId()),
                new UserId(e.getUserId()),
                new PlanId(e.getPlanId()),
                SubscriptionPeriod.of(e.getStartDate(), e.getEndDate()),
                SubscriptionStatus.valueOf(e.getStatus())
        );
    }
}