package com.example.billing.infrastructure.persistence.springdata;

import com.example.billing.infrastructure.persistence.entity.PlanEntity;
import com.example.billing.infrastructure.persistence.entity.PlanStatusJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataPlanRepository extends JpaRepository<PlanEntity, UUID> {
    List<PlanEntity> findAllByStatus(PlanStatusJpa status);

    @Query("SELECT p FROM PlanEntity p WHERE p.status = 'ACTIVE' ORDER BY p.priceAmount")
    List<PlanEntity> findAllActivePlans();

    boolean existsById(UUID id);
}