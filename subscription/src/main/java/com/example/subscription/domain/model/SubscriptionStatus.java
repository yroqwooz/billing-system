package com.example.subscription.domain.model;

public enum SubscriptionStatus {

    CREATED {
        @Override
        public boolean canTransitionTo(SubscriptionStatus target) {
            return target == ACTIVE || target == CANCELLED;
        }
    },
    ACTIVE {
        @Override
        public boolean canTransitionTo(SubscriptionStatus target) {
            return target == CANCELLED || target == EXPIRED;
        }
    },
    CANCELLED {
        @Override
        public boolean canTransitionTo(SubscriptionStatus target) {
            return false;
        }
    },
    EXPIRED {
        @Override
        public boolean canTransitionTo(SubscriptionStatus target) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(SubscriptionStatus target);
}
