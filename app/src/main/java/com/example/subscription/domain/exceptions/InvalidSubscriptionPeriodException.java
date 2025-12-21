package com.example.subscription.domain.exceptions;

public class InvalidSubscriptionPeriodException extends DomainException {

    public InvalidSubscriptionPeriodException() {
        super("Invalid subscription period");
    }
}