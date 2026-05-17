package com.kickrank.auth.dto;

import com.kickrank.common.enums.Role;

import java.util.Set;
import java.util.UUID;

public record AuthResponse(
        String token,
        UUID id,
        String fullName,
        String email,
        int elo,
        boolean organizerVerified,
        boolean observerVerified,
        Set<Role> roles
) {
}
