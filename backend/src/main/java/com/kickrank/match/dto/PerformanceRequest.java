package com.kickrank.match.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PerformanceRequest(
        @NotNull UUID playerId,
        @Min(0) Integer goals,
        @Min(0) Integer assists,
        @DecimalMin("1.0") @DecimalMax("10.0") Double observerRating
) {
}
