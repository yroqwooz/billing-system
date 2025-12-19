package com.example.subscription.web;

import com.example.subscription.application.SubscriptionService;
import com.example.subscription.domain.id.PlanId;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.domain.id.UserId;
import com.example.subscription.web.dto.CreateSubscriptionRequest;
import com.example.subscription.web.dto.CreateSubscriptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSubscriptionResponse create(
            @RequestBody CreateSubscriptionRequest request
    ) {
        SubscriptionId id = service.createSubscription(
                new UserId(request.userId()),
                new PlanId(request.planId())
        );

        return new CreateSubscriptionResponse(id.value());
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable UUID id) {
        service.cancelSubscription(new SubscriptionId(id));
    }
}