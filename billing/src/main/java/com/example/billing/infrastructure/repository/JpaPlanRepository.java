package com.example.billing.infrastructure.repository;

import com.example.billing.domain.plan.Plan;
import com.example.billing.domain.plan.PlanStatus;
import com.example.billing.infrastructure.persistence.entity.PlanEntity;
import com.example.billing.infrastructure.persistence.entity.PlanMapper;
import com.example.billing.infrastructure.persistence.entity.PlanStatusJpa;
import com.example.billing.infrastructure.persistence.springdata.SpringDataPlanRepository;
import com.example.common.PlanId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Адаптер для работы с планами через JPA.
 * Реализует порт PlanRepository, используя Spring Data JPA.
 */
@Repository
@Primary  // Использовать эту реализацию по умолчанию
@Transactional(readOnly = true)
public class JpaPlanRepository implements PlanRepository {

    private final SpringDataPlanRepository springDataRepository;

    public JpaPlanRepository(SpringDataPlanRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Optional<Plan> findById(PlanId id) {
        return springDataRepository.findById(id.value())
                .map(PlanMapper::toDomain);
    }

    @Override
    public List<Plan> findAllByStatus(PlanStatus status) {
        PlanStatusJpa jpaStatus = PlanStatusJpa.valueOf(status.name());
        return springDataRepository.findAllByStatus(jpaStatus)
                .stream()
                .map(PlanMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Plan> findAllActive() {
        return springDataRepository.findAllActivePlans()
                .stream()
                .map(PlanMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional  // Операции записи должны быть в транзакции
    public void save(Plan plan) {
        // Проверяем, существует ли уже entity
        PlanEntity entity = springDataRepository
                .findById(plan.getId().value())
                .orElseGet(() -> PlanMapper.toEntity(plan));

        // Обновляем поля entity из domain модели
        PlanMapper.updateEntity(entity, plan);

        // Сохраняем
        springDataRepository.save(entity);
        springDataRepository.flush();  // Принудительная синхронизация с БД
    }

    @Override
    @Transactional
    public void delete(PlanId id) {
        springDataRepository.findById(id.value()).ifPresent(entity -> {
            entity.setStatus(PlanStatusJpa.OBSOLETE);
            springDataRepository.save(entity);
        });
    }

    @Override
    public boolean existsById(PlanId id) {
        return springDataRepository.existsById(id.value());
    }
}