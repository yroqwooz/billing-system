package com.example.billing.infrastructure.repository;

import com.example.billing.domain.plan.PlanId;
import com.example.billing.domain.plan.Plan;
import org.springframework.stereotype.Repository;

@Repository
public class PlanRepository {

    public Plan findById(PlanId id) {
        return new Plan(null, null, null, null);
    }
}
