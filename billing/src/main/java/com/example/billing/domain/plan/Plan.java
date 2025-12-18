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

    public PlanId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlanDuration getDuration() {
        return duration;
    }

    public Money getPrice() {
        return price;
    }

    public PlanStatus getStatus() {
        return status;
    }

    public boolean isActive() {
        return this.status == PlanStatus.ACTIVE;
    }

    public boolean isObsolete() {
        return this.status == PlanStatus.OBSOLETE;
    }

    private void changeStatus(PlanStatus newStatus) {
        if (status == newStatus) {
            throw new IllegalStateException("Plan already " + newStatus);
        }

        switch (status) {
            case OBSOLETE -> throw new IllegalStateException(
                    "Cannot change status from OBSOLETE to " + newStatus
            );
            case ACTIVE, INACTIVE -> status = newStatus;
        }
    }

    public void setActive() {
        changeStatus(PlanStatus.ACTIVE);
    }

    public void setInactive() {
        changeStatus(PlanStatus.INACTIVE);
    }

    public void setObsolete() {
        changeStatus(PlanStatus.OBSOLETE);
    }

}
