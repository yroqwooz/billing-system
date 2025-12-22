package com.example.billing.infrastructure.repository;

import com.example.billing.domain.plan.Plan;
import com.example.billing.domain.plan.PlanStatus;
import com.example.common.PlanId;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {

    Optional<Plan> findById(PlanId id);

    List<Plan> findAllByStatus(PlanStatus status);

    List<Plan> findAllActive();

    void save(Plan plan);

    void delete(PlanId id);

    boolean existsById(PlanId id);
}