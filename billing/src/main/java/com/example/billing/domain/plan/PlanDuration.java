package com.example.billing.domain.plan;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class PlanDuration {
    private final Period period;

    private void validate() {
        if (period.isNegative() || period.isZero()) {
            throw new IllegalArgumentException("Plan duration must be positive");
        }
    }

    public PlanDuration(int years, int months, int days) {
        this.period = Period.of(years, months, days);
        validate();
    }

    public PlanDuration(Period period) {
        this.period = Objects.requireNonNull(period, "Period must not be null");
        validate();
    }

    public int getYears() {
        return period.getYears();
    }

    public int getMonths() {
        return period.getMonths();
    }

    public int getDays() {
        return period.getDays();
    }

    public LocalDate addTo(LocalDate startDate) {
        Objects.requireNonNull(startDate, "Start date must not be null");
        return startDate.plus(period);
    }

    public Period asPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanDuration)) return false;
        PlanDuration that = (PlanDuration) o;
        return period.equals(that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period);
    }

    @Override
    public String toString() {
        return period.toString();
    }

}
