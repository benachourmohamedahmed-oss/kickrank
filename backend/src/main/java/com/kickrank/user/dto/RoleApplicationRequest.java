package com.kickrank.user.dto;

import com.kickrank.common.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RoleApplicationRequest(
        @NotNull Role requestedRole,
        @NotBlank @Size(min = 20, max = 900) String motivation
) {
}
