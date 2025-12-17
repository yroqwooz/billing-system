package com.example.subscription.domain;

import java.time.LocalDate;
import java.util.Objects;

public final class SubscriptionPeriod {
    private final LocalDate startDate;
    private final LocalDate endDate;


    public SubscriptionPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = Objects.requireNonNull(startDate, "startDate must not be null");
        this.endDate = Objects.requireNonNull(endDate, "endDate must not be null");

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "The start date of the subscription cannot be later than its end date"
            );
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(this.startDate) && !date.isBefore(this.endDate);
    }

    public boolean overlapsWith(SubscriptionPeriod other) {
        return startDate.isBefore(other.endDate) && endDate.isAfter(other.startDate);
    }

}
