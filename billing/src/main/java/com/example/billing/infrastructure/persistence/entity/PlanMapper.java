package com.example.billing.infrastructure.persistence.entity;

import com.example.billing.domain.plan.*;
import com.example.common.PlanId;

import java.time.Period;
import java.util.Currency;

/**
 * Маппер для преобразования между доменной моделью и JPA сущностью.
 * Следует паттерну Data Mapper.
 */
public final class PlanMapper {

    private PlanMapper() {
        // Утилитный класс
    }

    /**
     * Преобразовать доменную модель в JPA сущность (для создания новой записи)
     */
    public static PlanEntity toEntity(Plan plan) {
        PlanEntity entity = new PlanEntity(plan.getId().value());
        updateEntity(entity, plan);
        return entity;
    }

    /**
     * Обновить существующую JPA сущность из доменной модели
     */
    public static void updateEntity(PlanEntity entity, Plan plan) {
        entity.setName(plan.getName());
        entity.setStatus(PlanStatusJpa.valueOf(plan.getStatus().name()));

        // Duration
        Period period = plan.getDuration().asPeriod();
        entity.setDurationYears(period.getYears());
        entity.setDurationMonths(period.getMonths());
        entity.setDurationDays(period.getDays());

        // Money
        entity.setPriceAmount(plan.getPrice().getAmount());
        entity.setPriceCurrency(plan.getPrice().getCurrency().getCurrencyCode());
    }

    /**
     * Преобразовать JPA сущность в доменную модель
     */
    public static Plan toDomain(PlanEntity entity) {
        PlanId id = new PlanId(entity.getId());
        String name = entity.getName();

        PlanDuration duration = new PlanDuration(
                entity.getDurationYears(),
                entity.getDurationMonths(),
                entity.getDurationDays()
        );

        Money price = new Money(
                entity.getPriceAmount(),
                Currency.getInstance(entity.getPriceCurrency())
        );

        PlanStatus status = PlanStatus.valueOf(entity.getStatus().name());

        // ✅ Используем restore() для восстановления из БД
        return Plan.restore(id, name, duration, price, status);
    }
}