package com.example.billing.application;

import com.example.billing.domain.plan.*;
import com.example.billing.infrastructure.repository.PlanRepository;
import com.example.common.PlanId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlanService")
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    private Money price;
    private PlanDuration duration;

    @BeforeEach
    void setUp() {
        price = new Money(new BigDecimal("9.99"), Currency.getInstance("USD"));
        duration = new PlanDuration(0, 1, 0);
    }

    @Test
    @DisplayName("should create plan with ACTIVE status")
    void shouldCreatePlanWithActiveStatus() {
        // Given
        String name = "Basic Plan";

        // When
        planService.createPlan(name, price, duration);

        // Then
        ArgumentCaptor<Plan> planCaptor = ArgumentCaptor.forClass(Plan.class);
        verify(planRepository).save(planCaptor.capture());

        Plan savedPlan = planCaptor.getValue();
        assertThat(savedPlan.getName()).isEqualTo(name);
        assertThat(savedPlan.getPrice()).isEqualTo(price);
        assertThat(savedPlan.getDuration()).isEqualTo(duration);
        assertThat(savedPlan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
    }

    @Test
    @DisplayName("should find existing plan")
    void shouldFindExistingPlan() {
        // Given
        PlanId planId = PlanId.newId();
        Plan expectedPlan = new Plan(planId, "Test Plan", duration, price);

        when(planRepository.findById(planId)).thenReturn(Optional.of(expectedPlan));

        // When
        Optional<Plan> result = planService.findPlan(planId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedPlan);
        verify(planRepository).findById(planId);
    }

    @Test
    @DisplayName("should return empty when plan not found")
    void shouldReturnEmptyWhenPlanNotFound() {
        // Given
        PlanId planId = PlanId.newId();
        when(planRepository.findById(planId)).thenReturn(Optional.empty());

        // When
        Optional<Plan> result = planService.findPlan(planId);

        // Then
        assertThat(result).isEmpty();
        verify(planRepository).findById(planId);
    }
}