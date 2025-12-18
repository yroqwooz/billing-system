package com.example.billing.domain.plan;

import java.util.Objects;

public class Plan {
    private final PlanId id;
    private final String name;
    private final PlanDuration duration;
    private final Money price;
    private PlanStatus status;

    public Plan(PlanId id, String name, PlanDuration duration, Money price) {
        this.id = Objects.requireNonNull(id, "id must be not null");
        this.name = Objects.requireNonNull(name, "Name must be not null");
        this.duration = Objects.requireNonNull(duration, "duration must be not null");
        this.price = Objects.requireNonNull(price, "price must be not null");
        this.status = PlanStatus.INACTIVE;
    }

    public void setActive() {
        switch (this.status) {
            case INACTIVE -> this.status = PlanStatus.ACTIVE;
            case OBSOLETE -> throw new IllegalStateException("Plan cannot be activated from obsolete state");
            case ACTIVE -> throw new IllegalStateException("Plan already active");
        }
    }

    public void setInactive() {
        switch (this.status) {
            case ACTIVE -> this.status = PlanStatus.INACTIVE;
            case OBSOLETE -> throw new IllegalStateException("Plan cannot become inactive from obsolete state");
            case INACTIVE -> throw new IllegalStateException("Plan already inactive");
        }
    }

    public void setObsolete() {
        switch (this.status) {
            case INACTIVE -> this.status = PlanStatus.OBSOLETE;
            case ACTIVE -> throw new IllegalStateException("Plan cannot become inactive from obsolete state");
            case OBSOLETE -> throw new IllegalStateException("Plan already obsolete");
        }
    }

}
