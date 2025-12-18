package com.example.billing.infrastructure.repository;

import org.springframework.stereotype.Service;

@Service
public class BillingPlanQueryImpl implements PlanQuery {
    private final PlanRepository planRepository; // billing internal repo

    public BillingPlanQueryImpl(PlanRepository planRepository) { this.planRepository = planRepository; }

    public Optional<PlanSnapshot> findById(PlanId id) {
        return planRepository.findById(id).map(plan ->
                new PlanSnapshot(plan.getId(), plan.getName(), plan.getDuration(), plan.getPrice(), plan.getStatus())
        );
    }
}
