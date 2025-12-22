package com.example.billing;

import com.example.billing.domain.plan.*;
import com.example.billing.infrastructure.repository.PlanRepository;
import com.example.common.PlanId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PlanRepositoryTest {

    @Autowired
    private PlanRepository repository;

    @Test
    void shouldSaveAndFindPlan() {
        // Given
        PlanId id = PlanId.newId();
        Money price = new Money(new BigDecimal("9.99"), Currency.getInstance("USD"));
        PlanDuration duration = new PlanDuration(0, 1, 0);
        Plan plan = new Plan(id, "Test Plan", duration, price);
        plan.setActive();

        // When
        repository.save(plan);
        Optional<Plan> found = repository.findById(id);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Plan");
        assertThat(found.get().getStatus()).isEqualTo(PlanStatus.ACTIVE);
    }
}