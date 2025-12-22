package com.example.subscription.application;

import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.model.SubscriptionStatus;
import com.example.subscription.infrastructure.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionExpirationService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionExpirationService.class);

    private final SubscriptionRepository repository;
    private final Clock clock;

    public SubscriptionExpirationService(
            SubscriptionRepository repository,
            Clock clock
    ) {
        this.repository = repository;
        this.clock = clock;
    }

    @Scheduled(cron = "${subscription.expiration.cron:0 0 1 * * ?}")
    @Transactional
    public void expireSubscriptions() {
        LocalDate today = LocalDate.now(clock);
        log.info("Running subscription expiration check for date: {}", today);

        List<Subscription> activeSubscriptions = repository.findAllByStatus(SubscriptionStatus.ACTIVE);

        int expiredCount = 0;
        for (Subscription subscription : activeSubscriptions) {
            if (subscription.getPeriod().endsBefore(today)) {
                subscription.expire(today);
                repository.save(subscription);
                expiredCount++;
                log.debug("Expired subscription: {}", subscription.getId());
            }
        }
        log.info("Expired {} subscriptions", expiredCount);
    }
}