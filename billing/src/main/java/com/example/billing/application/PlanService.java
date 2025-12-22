package com.example.billing.application;

import com.example.billing.domain.plan.Money;
import com.example.billing.domain.plan.Plan;
import com.example.billing.domain.plan.PlanDuration;
import com.example.billing.infrastructure.repository.PlanRepository;
import com.example.common.PlanId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public void createPlan(String name, Money price, PlanDuration duration) {
        Plan plan = new Plan(PlanId.newId(), name, duration, price);
        plan.setActive();
        planRepository.save(plan);  // ✅ Работает!
    }

    public Optional<Plan> findPlan(PlanId id) {
        return planRepository.findById(id);  // ✅ Работает!
    }
}