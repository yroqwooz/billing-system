package com.example.subscription.application.port;

import com.example.subscription.domain.id.PlanId;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface PlanQuery {
    Optional<PlanSnapshot> getPlan(PlanId planId);
}
