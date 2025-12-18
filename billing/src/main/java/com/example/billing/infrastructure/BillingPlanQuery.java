package com.example.billing.infrastructure;

import com.example.billing.domain.plan.Plan;
import com.example.billing.infrastructure.repository.PlanRepository;
import com.example.common.PlanId;
import com.example.common.PlanQuery;
import com.example.common.PlanSnapshot;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BillingPlanQuery implements PlanQuery {
    private final PlanRepository repository;
    @Override
    public Optional<PlanSnapshot> findById(PlanId id) {
        Plan plan = repository.findById(id)
                .orElseThrow(...);

        return new PlanSnapshot(
                plan.getId(),
                plan.getDuration(),
                plan.getPrice(),
                plan.getStatus()
        );
    }
}
