
package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.app",
        "com.example.subscription",
        "com.example.billing"
})
@EnableScheduling
@EnableRetry
public class BillingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingSystemApplication.class, args);
    }
}