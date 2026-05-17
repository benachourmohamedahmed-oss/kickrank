package com.kickrank.match.dto;

import com.kickrank.common.enums.MatchFormat;
import com.kickrank.common.enums.MatchType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateMatchRequest(
        @NotBlank @Size(min = 4, max = 140) String title,
        @NotBlank @Size(min = 3, max = 180) String location,
        @NotNull @Future LocalDateTime scheduledAt,
        @NotNull MatchType type,
        @NotNull MatchFormat format,
        UUID observerId
) {
}
