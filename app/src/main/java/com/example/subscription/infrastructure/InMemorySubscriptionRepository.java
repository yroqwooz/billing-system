package com.example.subscription.infrastructure;

import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.id.UserId;
import com.example.subscription.domain.model.Subscription;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemorySubscriptionRepository
        implements SubscriptionRepository {

    private final Map<SubscriptionId, Subscription> storage = new HashMap<>();

    @Override
    public Optional<Subscription> findById(SubscriptionId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Subscription> findByUserId(UserId userId) {
        return storage.values().stream()
                .filter(s -> s.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void save(Subscription subscription) {
        storage.put(subscription.getId(), subscription);
    }
}