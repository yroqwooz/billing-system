package com.example.billing.infrastructure.repository;

import com.example.billing.domain.plan.Plan;
import com.example.billing.domain.plan.PlanStatus;
import com.example.common.PlanId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


// In-memory реализация для тестов и разработки.
@Repository
@Profile("test")
public class InMemoryPlanRepository implements PlanRepository {

    private final Map<PlanId, Plan> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Plan> findById(PlanId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Plan> findAllByStatus(PlanStatus status) {
        return storage.values().stream()
                .filter(plan -> plan.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Plan> findAllActive() {
        return findAllByStatus(PlanStatus.ACTIVE);
    }

    @Override
    public void save(Plan plan) {
        storage.put(plan.getId(), plan);
    }

    @Override
    public void delete(PlanId id) {
        findById(id).ifPresent(plan -> {
            plan.setObsolete();
            save(plan);
        });
    }

    @Override
    public boolean existsById(PlanId id) {
        return storage.containsKey(id);
    }

    // Дополнительные методы для тестов
    public void clear() {
        storage.clear();
    }

    public int size() {
        return storage.size();
    }
}