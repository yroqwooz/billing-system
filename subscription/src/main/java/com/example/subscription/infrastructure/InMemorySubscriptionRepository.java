package com.example.subscription.infrastructure;

import com.example.subscription.domain.id.SubscriptionId;
import com.example.common.UserId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.model.SubscriptionStatus;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("test")
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

    @Override
    public List<Subscription> findAllByStatus(SubscriptionStatus status) {
        return storage.values().stream()
                .filter(s -> s.getStatus() == status)
                .collect(Collectors.toList());
    }
}