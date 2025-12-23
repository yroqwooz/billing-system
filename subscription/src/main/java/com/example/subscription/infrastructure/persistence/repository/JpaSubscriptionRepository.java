package com.example.subscription.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.subscription.domain.id.SubscriptionId;
import com.example.common.UserId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.model.SubscriptionStatus;
import com.example.subscription.infrastructure.SubscriptionRepository;
import com.example.subscription.infrastructure.persistence.entity.SubscriptionEntity;
import com.example.subscription.infrastructure.persistence.entity.SubscriptionMapper;
import com.example.subscription.infrastructure.persistence.entity.SubscriptionStatusJpa;
import com.example.subscription.infrastructure.persistence.springdata.SpringDataSubscriptionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("!test")
@Repository
@Primary
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
        SubscriptionEntity entity = delegate
                .findById(subscription.getId().value())
                .orElseGet(() -> SubscriptionMapper.toEntity(subscription));

        SubscriptionMapper.updateEntity(entity, subscription);

        delegate.save(entity);
        delegate.flush();
    }

    @Override
    public List<Subscription> findAllByStatus(SubscriptionStatus status) {
        SubscriptionStatusJpa jpaStatus = SubscriptionStatusJpa.valueOf(status.name());

        return delegate.findAllByStatus(jpaStatus).stream()
                .map(SubscriptionMapper::toDomain)
                .collect(Collectors.toList());
    }
}