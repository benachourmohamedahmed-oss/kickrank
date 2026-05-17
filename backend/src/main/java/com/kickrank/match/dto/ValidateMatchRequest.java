package com.kickrank.match.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ValidateMatchRequest(
        @NotNull @Min(0) Integer teamAScore,
        @NotNull @Min(0) Integer teamBScore,
        @Valid List<PerformanceRequest> performances
) {
}
