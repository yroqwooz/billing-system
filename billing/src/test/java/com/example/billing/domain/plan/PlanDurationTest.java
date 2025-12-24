package com.example.billing.domain.plan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("PlanDuration value object")
class PlanDurationTest {

    @Test
    @DisplayName("should create duration with years, months, days")
    void shouldCreateDurationWithComponents() {
        // When
        PlanDuration duration = new PlanDuration(1, 2, 15);

        // Then
        assertThat(duration.getYears()).isEqualTo(1);
        assertThat(duration.getMonths()).isEqualTo(2);
        assertThat(duration.getDays()).isEqualTo(15);
    }

    @Test
    @DisplayName("should create duration from Period")
    void shouldCreateDurationFromPeriod() {
        // Given
        Period period = Period.of(0, 6, 0);

        // When
        PlanDuration duration = new PlanDuration(period);

        // Then
        assertThat(duration.getMonths()).isEqualTo(6);
        assertThat(duration.asPeriod()).isEqualTo(period);
    }

    @Test
    @DisplayName("should not allow zero duration")
    void shouldNotAllowZeroDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> new PlanDuration(0, 0, 0),
                "Plan duration must be positive"
        );
    }

    @Test
    @DisplayName("should not allow negative duration")
    void shouldNotAllowNegativeDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> new PlanDuration(-1, 0, 0)
        );
    }

    @Test
    @DisplayName("should correctly add to date")
    void shouldCorrectlyAddToDate() {
        // Given
        PlanDuration duration = new PlanDuration(0, 1, 0); // 1 month
        LocalDate start = LocalDate.of(2024, 1, 31);

        // When
        LocalDate end = duration.addTo(start);

        // Then
        assertThat(end).isEqualTo(LocalDate.of(2024, 2, 29)); // Leap year
    }

    @Test
    @DisplayName("should handle year duration correctly")
    void shouldHandleYearDurationCorrectly() {
        // Given
        PlanDuration duration = new PlanDuration(1, 0, 0);
        LocalDate start = LocalDate.of(2024, 2, 29); // Leap day

        // When
        LocalDate end = duration.addTo(start);

        // Then
        assertThat(end).isEqualTo(LocalDate.of(2025, 2, 28)); // Not a leap year
    }

    @Test
    @DisplayName("should be equal with same period")
    void shouldBeEqualWithSamePeriod() {
        // Given
        PlanDuration duration1 = new PlanDuration(1, 2, 3);
        PlanDuration duration2 = new PlanDuration(1, 2, 3);

        // When & Then
        assertThat(duration1).isEqualTo(duration2);
        assertThat(duration1.hashCode()).isEqualTo(duration2.hashCode());
    }
}