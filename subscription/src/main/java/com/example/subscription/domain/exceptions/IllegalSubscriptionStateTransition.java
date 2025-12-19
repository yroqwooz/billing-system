package com.example.subscription.domain.exceptions;

import com.example.subscription.domain.model.SubscriptionStatus;

public class IllegalSubscriptionStateTransition extends DomainException {

    public IllegalSubscriptionStateTransition(
            SubscriptionStatus from,
            SubscriptionStatus to
    ) {
        super("Cannot transition subscription from " + from + " to " + to);
    }
}
