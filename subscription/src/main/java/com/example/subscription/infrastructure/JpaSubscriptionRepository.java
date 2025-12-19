package com.example.subscription.infrastructure;

import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.id.UserId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.infrastructure.persistence.entity.SubscriptionMapper;
import com.example.subscription.infrastructure.persistence.springdata.SpringDataSubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class JpaSubscriptionRepository implements SubscriptionRepository {

    private final SpringDataSubscriptionRepository delegate;

    JpaSubscriptionRepository(SpringDataSubscriptionRepository delegate) {
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
