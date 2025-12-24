package com.example.subscription.domain.model;

import com.example.common.PlanId;
import com.example.common.UserId;
import com.example.subscription.domain.exceptions.IllegalSubscriptionStateTransition;
import com.example.subscription.domain.id.SubscriptionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Subscription aggregate")
class SubscriptionTest {

    private SubscriptionId subscriptionId;
    private UserId userId;
    private PlanId planId;
    private SubscriptionPeriod period;

    @BeforeEach
    void setUp() {
        subscriptionId = SubscriptionId.newId();
        userId = UserId.newId();
        planId = PlanId.newId();

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 2, 1);
        period = SubscriptionPeriod.of(start, end);
    }

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("should create subscription with CREATED status")
        void shouldCreateSubscriptionWithCreatedStatus() {
            // When
            Subscription subscription = Subscription.create(
                    subscriptionId, userId, planId, period
            );

            // Then
            assertThat(subscription.getId()).isEqualTo(subscriptionId);
            assertThat(subscription.getUserId()).isEqualTo(userId);
            assertThat(subscription.getPlanId()).isEqualTo(planId);
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.CREATED);
            assertThat(subscription.version()).isEqualTo(0L);
        }
    }

    @Nested
    @DisplayName("Status transitions")
    class StatusTransitions {

        private Subscription subscription;

        @BeforeEach
        void setUp() {
            subscription = Subscription.create(subscriptionId, userId, planId, period);
        }

        @Test
        @DisplayName("should activate subscription")
        void shouldActivateSubscription() {
            // When
            subscription.activate();

            // Then
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        }

        @Test
        @DisplayName("should cancel active subscription")
        void shouldCancelActiveSubscription() {
            // Given
            subscription.activate();

            // When
            subscription.cancel();

            // Then
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.CANCELLED);
        }

        @Test
        @DisplayName("should be idempotent when cancelling already cancelled subscription")
        void shouldBeIdempotentWhenCancellingAlreadyCancelled() {
            // Given
            subscription.activate();
            subscription.cancel();

            // When - cancel again
            subscription.cancel();

            // Then - no exception, still cancelled
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.CANCELLED);
        }

        @Test
        @DisplayName("should expire active subscription after end date")
        void shouldExpireActiveSubscriptionAfterEndDate() {
            // Given
            subscription.activate();
            LocalDate afterEnd = period.endDate().plusDays(1);

            // When
            subscription.expire(afterEnd);

            // Then
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.EXPIRED);
        }

        @Test
        @DisplayName("should not expire subscription before end date")
        void shouldNotExpireSubscriptionBeforeEndDate() {
            // Given
            subscription.activate();
            LocalDate beforeEnd = period.endDate().minusDays(1);

            // When
            subscription.expire(beforeEnd);

            // Then
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        }

        @Test
        @DisplayName("should not allow transition from CREATED to EXPIRED")
        void shouldNotAllowTransitionFromCreatedToExpired() {
            // When & Then
            assertThrows(IllegalSubscriptionStateTransition.class,
                    () -> subscription.expire(LocalDate.now())
            );
        }

        @Test
        @DisplayName("should not allow transition from CANCELLED to ACTIVE")
        void shouldNotAllowTransitionFromCancelledToActive() {
            // Given
            subscription.activate();
            subscription.cancel();

            // When & Then
            assertThrows(IllegalSubscriptionStateTransition.class,
                    () -> subscription.activate()
            );
        }
    }

    @Nested
    @DisplayName("Restore from persistence")
    class RestoreFromPersistence {

        @Test
        @DisplayName("should restore subscription with all fields")
        void shouldRestoreSubscriptionWithAllFields() {
            // When
            Subscription restored = Subscription.restore(
                    subscriptionId,
                    userId,
                    planId,
                    SubscriptionStatus.ACTIVE,
                    period,
                    5L
            );

            // Then
            assertThat(restored.getId()).isEqualTo(subscriptionId);
            assertThat(restored.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
            assertThat(restored.version()).isEqualTo(5L);
        }
    }
}