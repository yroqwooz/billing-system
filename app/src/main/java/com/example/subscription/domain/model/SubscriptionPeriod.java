package com.example.subscription.domain.model;

import com.example.subscription.domain.exceptions.InvalidSubscriptionPeriodException;

import java.time.LocalDate;

public class SubscriptionPeriod {

    private final LocalDate start;
    private final LocalDate end;

    private SubscriptionPeriod(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate startDate() {
        return start;
    }

    public LocalDate endDate() {
        return end;
    }

    public static SubscriptionPeriod of(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new InvalidSubscriptionPeriodException();
        }
        return new SubscriptionPeriod(start, end);
    }

    public boolean endsBefore(LocalDate date) {
        return end.isBefore(date);
    }


}

