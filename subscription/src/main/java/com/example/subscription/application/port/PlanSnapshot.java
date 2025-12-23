package com.example.subscription.application.port;


import com.example.common.PlanId;

import java.time.Period;


public record PlanSnapshot(
        PlanId id,
        Period duration
) {}
