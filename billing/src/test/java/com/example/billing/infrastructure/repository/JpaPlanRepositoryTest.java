package com.example.billing.infrastructure.repository;

import com.example.billing.domain.plan.*;
import com.example.common.PlanId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaPlanRepository.class})  // Import repository implementation
@ActiveProfiles("test")
@DisplayName("JpaPlanRepository integration tests")
class JpaPlanRepositoryTest {

    @Autowired
    private PlanRepository repository;

    @Test
    @DisplayName("should save and find plan by id")
    void shouldSaveAndFindPlanById() {
        // Given
        PlanId id = PlanId.newId();
        Money price = new Money(new BigDecimal("19.99"), Currency.getInstance("USD"));
        PlanDuration duration = new PlanDuration(1, 0, 0);

        Plan plan = new Plan(id, "Premium Yearly", duration, price);
        plan.setActive();

        // When
        repository.save(plan);
        Optional<Plan> found = repository.findById(id);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(id);
        assertThat(found.get().getName()).isEqualTo("Premium Yearly");
        assertThat(found.get().getStatus()).isEqualTo(PlanStatus.ACTIVE);
        assertThat(found.get().getPrice().getAmount()).isEqualByComparingTo("19.99");
    }

    @Test
    @DisplayName("should find all active plans")
    void shouldFindAllActivePlans() {
        // Given
        createAndSavePlan("Active 1", PlanStatus.ACTIVE);
        createAndSavePlan("Active 2", PlanStatus.ACTIVE);
        createAndSavePlan("Inactive", PlanStatus.INACTIVE);

        // When
        List<Plan> activePlans = repository.findAllActive();

        // Then
        assertThat(activePlans).hasSize(2);
        assertThat(activePlans).allMatch(Plan::isActive);
    }

    @Test
    @DisplayName("should update plan status")
    void shouldUpdatePlanStatus() {
        // Given
        Plan plan = createAndSavePlan("Test Plan", PlanStatus.ACTIVE);

        // When
        plan.setInactive();
        repository.save(plan);

        // Then
        Optional<Plan> updated = repository.findById(plan.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getStatus()).isEqualTo(PlanStatus.INACTIVE);
    }

    @Test
    @DisplayName("should soft delete plan by setting OBSOLETE status")
    void shouldSoftDeletePlan() {
        // Given
        Plan plan = createAndSavePlan("To Delete", PlanStatus.ACTIVE);

        // When
        repository.delete(plan.getId());

        // Then
        Optional<Plan> deleted = repository.findById(plan.getId());
        assertThat(deleted).isPresent();
        assertThat(deleted.get().getStatus()).isEqualTo(PlanStatus.OBSOLETE);
    }

    private Plan createAndSavePlan(String name, PlanStatus status) {
        Money price = new Money(new BigDecimal("9.99"), Currency.getInstance("USD"));
        PlanDuration duration = new PlanDuration(0, 1, 0);

        Plan plan = new Plan(PlanId.newId(), name, duration, price);

        switch (status) {
            case ACTIVE -> plan.setActive();
//            case INACTIVE -> plan.setInactive();
            case OBSOLETE -> {
                plan.setActive();
                plan.setObsolete();
            }
        }

        repository.save(plan);
        return plan;
    }
}