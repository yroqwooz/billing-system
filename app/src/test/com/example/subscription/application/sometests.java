package com.example.subscription.application;

import java.util.concurrent.CountDownLatch;

//    @Test
//    void shouldFailOnOptimisticLocking() throws Exception {
//        UserId userId = new UserId(UUID.randomUUID());
//        PlanId planId = new PlanId(UUID.randomUUID());
//
//        SubscriptionId id =
//                subscriptionService.createSubscription(userId, planId);
//
//        CountDownLatch readLatch = new CountDownLatch(2);
//        CountDownLatch writeLatch = new CountDownLatch(1);
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        Future<?> tx1 = executor.submit(() -> {
//            new TransactionTemplate(transactionManager).execute(status -> {
//                Subscription s =
//                        subscriptionRepository.findById(id).orElseThrow();
//                readLatch.countDown();
//                await(readLatch);
//                s.cancel();
//                subscriptionRepository.save(s);
//                return null;
//            });
//        });
//
//        Future<?> tx2 = executor.submit(() -> {
//            new TransactionTemplate(transactionManager).execute(status -> {
//                Subscription s =
//                        subscriptionRepository.findById(id).orElseThrow();
//                readLatch.countDown();
//                await(readLatch);
//                s.cancel();
//                subscriptionRepository.save(s);
//                return null;
//            });
//        });
//
//        tx1.get(); // первый поток должен пройти без ошибок
//        assertThatThrownBy(() -> tx2.get())
//                .hasCauseInstanceOf(OptimisticLockingFailureException.class);
//
//
////        assertThatThrownBy(() -> {
////            tx1.get();
////            tx2.get();
////        }).hasCauseInstanceOf(OptimisticLockingFailureException.class);
//    }

//    private static void await(CountDownLatch latch) {
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}