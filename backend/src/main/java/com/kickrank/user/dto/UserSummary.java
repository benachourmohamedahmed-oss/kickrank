package com.kickrank.user.dto;

import com.kickrank.common.enums.Role;
import com.kickrank.user.entity.User;

import java.util.Set;
import java.util.UUID;

public record UserSummary(
        UUID id,
        String fullName,
        String email,
        int elo,
        boolean organizerVerified,
        boolean observerVerified,
        int organizerLevel,
        Set<Role> roles
) {
    public static UserSummary from(User user) {
        return new UserSummary(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getElo(),
                user.isOrganizerVerified(),
                user.isObserverVerified(),
                user.getOrganizerLevel(),
                user.getRoles()
        );
    }
}
