package com.example.billing.domain.plan;

import com.example.common.PlanId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Plan aggregate")
class PlanTest {

    private PlanId planId;
    private Money price;
    private PlanDuration duration;

    @BeforeEach
    void setUp() {
        planId = PlanId.newId();
        price = new Money(new BigDecimal("9.99"), Currency.getInstance("USD"));
        duration = new PlanDuration(0, 1, 0);
    }

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("should create plan with INACTIVE status")
        void shouldCreatePlanWithInactiveStatus() {
            // When
            Plan plan = new Plan(planId, "Basic Plan", duration, price);

            // Then
            assertThat(plan.getId()).isEqualTo(planId);
            assertThat(plan.getName()).isEqualTo("Basic Plan");
            assertThat(plan.getStatus()).isEqualTo(PlanStatus.INACTIVE);
            assertThat(plan.isActive()).isFalse();
        }

        @Test
        @DisplayName("should not allow null id")
        void shouldNotAllowNullId() {
            assertThrows(NullPointerException.class,
                    () -> new Plan(null, "Plan", duration, price)
            );
        }

        @Test
        @DisplayName("should not allow null name")
        void shouldNotAllowNullName() {
            assertThrows(NullPointerException.class,
                    () -> new Plan(planId, null, duration, price)
            );
        }

        @Test
        @DisplayName("should not allow null duration")
        void shouldNotAllowNullDuration() {
            assertThrows(NullPointerException.class,
                    () -> new Plan(planId, "Plan", null, price)
            );
        }

        @Test
        @DisplayName("should not allow null price")
        void shouldNotAllowNullPrice() {
            assertThrows(NullPointerException.class,
                    () -> new Plan(planId, "Plan", duration, null)
            );
        }
    }

    @Nested
    @DisplayName("Status transitions")
    class StatusTransitions {

        private Plan plan;

        @BeforeEach
        void setUp() {
            plan = new Plan(planId, "Test Plan", duration, price);
        }

        @Test
        @DisplayName("should transition from INACTIVE to ACTIVE")
        void shouldTransitionFromInactiveToActive() {
            // When
            plan.setActive();

            // Then
            assertThat(plan.getStatus()).isEqualTo(PlanStatus.ACTIVE);
            assertThat(plan.isActive()).isTrue();
        }

        @Test
        @DisplayName("should transition from ACTIVE to INACTIVE")
        void shouldTransitionFromActiveToInactive() {
            // Given
            plan.setActive();

            // When
            plan.setInactive();

            // Then
            assertThat(plan.getStatus()).isEqualTo(PlanStatus.INACTIVE);
            assertThat(plan.isActive()).isFalse();
        }

        @Test
        @DisplayName("should transition from ACTIVE to OBSOLETE")
        void shouldTransitionFromActiveToObsolete() {
            // Given
            plan.setActive();

            // When
            plan.setObsolete();

            // Then
            assertThat(plan.getStatus()).isEqualTo(PlanStatus.OBSOLETE);
            assertThat(plan.isObsolete()).isTrue();
        }

        @Test
        @DisplayName("should not allow setting same status twice")
        void shouldNotAllowSettingSameStatusTwice() {
            // Given
            plan.setActive();

            // When & Then
            assertThrows(IllegalStateException.class,
                    () -> plan.setActive(),
                    "Plan already ACTIVE"
            );
        }

        @Test
        @DisplayName("should not allow transition from OBSOLETE to ACTIVE")
        void shouldNotAllowTransitionFromObsoleteToActive() {
            // Given
            plan.setActive();
            plan.setObsolete();

            // When & Then
            assertThrows(IllegalStateException.class,
                    () -> plan.setActive(),
                    "Cannot change status from OBSOLETE"
            );
        }

        @Test
        @DisplayName("should not allow transition from OBSOLETE to INACTIVE")
        void shouldNotAllowTransitionFromObsoleteToInactive() {
            // Given
            plan.setActive();
            plan.setObsolete();

            // When & Then
            assertThrows(IllegalStateException.class,
                    () -> plan.setInactive()
            );
        }
    }
}