package com.example.subscription.infrastructure.persistence.springdata;

import java.util.Optional;
import java.util.UUID;

import com.example.subscription.infrastructure.persistence.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSubscriptionRepository
        extends JpaRepository<SubscriptionEntity, UUID> {

    Optional<SubscriptionEntity> findByUserId(UUID userId);
}