package com.example.subscription.application;

import com.example.common.PlanId;
import com.example.common.UserId;
import com.example.subscription.application.exceptions.PlanNotFoundException;
import com.example.subscription.application.exceptions.SubscriptionNotFoundException;
import com.example.subscription.application.port.PlanQuery;
import com.example.subscription.application.port.PlanSnapshot;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.model.SubscriptionPeriod;
import com.example.subscription.domain.model.SubscriptionStatus;
import com.example.subscription.infrastructure.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubscriptionService")
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository repository;

    @Mock
    private PlanQuery planQuery;

    @Mock
    private Clock clock;

    @InjectMocks
    private SubscriptionService service;

    private UserId userId;
    private PlanId planId;
    private LocalDate fixedDate;

    @BeforeEach
    void setUp() {
        userId = UserId.newId();
        planId = PlanId.newId();
        fixedDate = LocalDate.of(2024, 1, 15);

        // Mock clock to return fixed date
        Clock fixedClock = Clock.fixed(
                fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    @DisplayName("should create subscription with correct period")
    void shouldCreateSubscriptionWithCorrectPeriod() {
        // Given
        Period duration = Period.ofMonths(1);
        PlanSnapshot planSnapshot = new PlanSnapshot(planId, duration);

        when(planQuery.getPlan(planId)).thenReturn(Optional.of(planSnapshot));

        // When
        service.createSubscription(userId, planId);

        // Then
        ArgumentCaptor<Subscription> captor = ArgumentCaptor.forClass(Subscription.class);
        verify(repository).save(captor.capture());

        Subscription saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getPlanId()).isEqualTo(planId);
        assertThat(saved.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(saved.getPeriod().startDate()).isEqualTo(fixedDate);
        assertThat(saved.getPeriod().endDate()).isEqualTo(fixedDate.plusMonths(1));
    }

    @Test
    @DisplayName("should throw exception when plan not found")
    void shouldThrowExceptionWhenPlanNotFound() {
        // Given
        when(planQuery.getPlan(planId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PlanNotFoundException.class,
                () -> service.createSubscription(userId, planId)
        );

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("should cancel existing subscription")
    void shouldCancelExistingSubscription() {
        // Given
        SubscriptionId subscriptionId = SubscriptionId.newId();
        Subscription subscription = Subscription.create(
                subscriptionId,
                userId,
                planId,
                SubscriptionPeriod.of(fixedDate, fixedDate.plusMonths(1))
        );
        subscription.activate();

        when(repository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        // When
        service.cancelSubscription(subscriptionId);

        // Then
        verify(repository).save(subscription);
        assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.CANCELLED);
    }

    @Test
    @DisplayName("should throw exception when cancelling non-existent subscription")
    void shouldThrowExceptionWhenCancellingNonExistent() {
        // Given
        SubscriptionId subscriptionId = SubscriptionId.newId();
        when(repository.findById(subscriptionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(SubscriptionNotFoundException.class,
                () -> service.cancelSubscription(subscriptionId)
        );
    }
}