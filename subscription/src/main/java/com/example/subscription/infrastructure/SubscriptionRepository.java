package com.example.subscription.infrastructure;

import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.id.UserId;

import java.util.Optional;

public interface SubscriptionRepository {

    Optional<Subscription> findById(SubscriptionId id);

    Optional<Subscription> findByUserId(UserId userId);

    void save(Subscription subscription);
}
