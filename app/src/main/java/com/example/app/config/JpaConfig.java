package com.example.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.example.subscription.infrastructure.persistence.springdata",
        "com.example.billing.infrastructure.persistence.springdata"
})
@EntityScan(basePackages = {
        "com.example.subscription.infrastructure.persistence.entity",
        "com.example.billing.infrastructure.persistence.entity"
})
public class JpaConfig {
    // Конфигурация для сканирования JPA сущностей и репозиториев
}