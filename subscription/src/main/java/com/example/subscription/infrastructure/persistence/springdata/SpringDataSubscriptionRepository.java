package com.example.subscription.infrastructure.persistence.springdata;

import java.util.Optional;
import java.util.UUID;

import com.example.subscription.infrastructure.persistence.entity.SubscriptionEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSubscriptionRepository
        extends JpaRepository<SubscriptionEntity, UUID> {

    Optional<SubscriptionEntity> findByUserId(UUID userId);
}