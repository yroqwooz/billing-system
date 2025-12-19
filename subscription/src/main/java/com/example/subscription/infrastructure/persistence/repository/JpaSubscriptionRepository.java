package com.example.subscription.infrastructure.persistence.repository;

import java.util.Optional;

import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.id.UserId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.infrastructure.SubscriptionRepository;
import com.example.subscription.infrastructure.persistence.entity.SubscriptionMapper;
import com.example.subscription.infrastructure.persistence.springdata.SpringDataSubscriptionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("test")
@Repository
public class JpaSubscriptionRepository implements SubscriptionRepository {

    private final SpringDataSubscriptionRepository delegate;

    public JpaSubscriptionRepository(
            SpringDataSubscriptionRepository delegate
    ) {
        this.delegate = delegate;
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId id) {
        return delegate.findById(id.value())
                .map(SubscriptionMapper::toDomain);
    }

    @Override
    public Optional<Subscription> findByUserId(UserId userId) {
        return delegate.findByUserId(userId.value())
                .map(SubscriptionMapper::toDomain);
    }

    @Override
    public void save(Subscription subscription) {
        delegate.save(SubscriptionMapper.toEntity(subscription));
    }
}