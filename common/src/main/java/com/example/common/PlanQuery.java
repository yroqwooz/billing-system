package com.example.common;

import java.util.Optional;

public interface PlanQuery {
    Optional<PlanSnapshot> findById(PlanId id);
}
