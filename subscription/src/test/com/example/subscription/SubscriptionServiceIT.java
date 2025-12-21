package com.example.subscription;

import com.example.common.PlanId;
import com.example.common.UserId;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.model.Subscription;
import com.example.subscription.domain.model.SubscriptionPeriod;
import com.example.subscription.infrastructure.persistence.entity.SubscriptionEntity;
import com.example.subscription.infrastructure.persistence.entity.SubscriptionMapper;
import com.example.subscription.infrastructure.persistence.springdata.SpringDataSubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableJpaRepositories(basePackages = "com.example.subscription.infrastructure.persistence.springdata")
@EntityScan(basePackages = "com.example.subscription.infrastructure.persistence.entity")
class SubscriptionServiceOptimisticLockingIT {
    @Autowired
    private SpringDataSubscriptionRepository springDataSubscriptionRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void shouldFailOnOptimisticLocking() throws Exception {
        // Создаём доменную модель
        Subscription domain = Subscription.create(
                SubscriptionId.newId(),
                new UserId(UUID.randomUUID()),
                new PlanId(UUID.randomUUID()),
                SubscriptionPeriod.of(LocalDate.now(), LocalDate.now().plusDays(30))
        );
        domain.activate();

        // Конвертируем в сущность и сохраняем
        SubscriptionEntity entity = SubscriptionMapper.toEntity(domain);
        springDataSubscriptionRepository.saveAndFlush(entity);

        // Синхронизация потоков
        CountDownLatch readyLatch = new CountDownLatch(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        var future1 = executor.submit(() -> new TransactionTemplate(transactionManager).execute(status -> {
            SubscriptionEntity s = springDataSubscriptionRepository.findById(entity.getId()).orElseThrow();
            readyLatch.countDown();
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            s.setEndDate(s.getEndDate().plusDays(1)); // реальное изменение
            springDataSubscriptionRepository.saveAndFlush(s);
            return null;
        }));

        var future2 = executor.submit(() -> new TransactionTemplate(transactionManager).execute(status -> {
            SubscriptionEntity s = springDataSubscriptionRepository.findById(entity.getId()).orElseThrow();
            readyLatch.countDown();
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            s.setEndDate(s.getEndDate().plusDays(2)); // реальное изменение
            springDataSubscriptionRepository.saveAndFlush(s);
            return null;
        }));

        // Ждём, пока оба потока готовы, и запускаем одновременно
        readyLatch.await();
        startLatch.countDown();

        // Проверяем, что один из потоков выбросил ObjectOptimisticLockingFailureException
        ExecutionException exception = assertThrows(ExecutionException.class, () -> {
            future1.get();
            future2.get();
        });

        assertThat(exception.getCause()).isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
