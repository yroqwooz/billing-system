package com.example.subscription.domain;

import java.time.LocalDate;

public final class SubscriptionPeriod {
    private final LocalDate startDate;
    private final LocalDate endDate;


    public SubscriptionPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "The start date of the subscription cannot be later than its end date"
            );
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
