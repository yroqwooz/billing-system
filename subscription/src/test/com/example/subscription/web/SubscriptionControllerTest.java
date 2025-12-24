package com.example.subscription.web;

import com.example.common.PlanId;
import com.example.common.UserId;
import com.example.subscription.application.SubscriptionService;
import com.example.subscription.domain.id.SubscriptionId;
import com.example.subscription.web.dto.CreateSubscriptionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
@DisplayName("SubscriptionController")
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService service;

    @Test
    @DisplayName("POST /subscriptions should create subscription")
    void shouldCreateSubscription() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();

        CreateSubscriptionRequest request = new CreateSubscriptionRequest(userId, planId);

        when(service.createSubscription(any(UserId.class), any(PlanId.class)))
                .thenReturn(new SubscriptionId(subscriptionId));

        // When & Then
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subscriptionId").value(subscriptionId.toString()));

        verify(service).createSubscription(new UserId(userId), new PlanId(planId));
    }

    @Test
    @DisplayName("POST /subscriptions should return 400 for invalid request")
    void shouldReturn400ForInvalidRequest() throws Exception {
        // Given - request without userId
        String invalidJson = "{\"planId\": \"" + UUID.randomUUID() + "\"}";

        // When & Then
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /subscriptions/{id}/cancel should cancel subscription")
    void shouldCancelSubscription() throws Exception {
        // Given
        UUID subscriptionId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(post("/subscriptions/" + subscriptionId + "/cancel"))
                .andExpect(status().isNoContent());

        verify(service).cancelSubscription(new SubscriptionId(subscriptionId));
    }
}