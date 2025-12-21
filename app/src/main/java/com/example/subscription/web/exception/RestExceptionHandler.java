package com.example.subscription.web.exception;

import com.example.subscription.application.exceptions.PlanNotFoundException;
import com.example.subscription.application.exceptions.SubscriptionNotFoundException;
import com.example.subscription.domain.exceptions.DomainException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SubscriptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse subscriptionNotFound() {
        return new ErrorResponse("SUBSCRIPTION_NOT_FOUND");
    }

    @ExceptionHandler(PlanNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse planNotFound() {
        return new ErrorResponse("PLAN_NOT_FOUND");
    }

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse domainError(DomainException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}