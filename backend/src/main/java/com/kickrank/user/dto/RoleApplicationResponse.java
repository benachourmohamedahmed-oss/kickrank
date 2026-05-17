package com.kickrank.user.dto;

import com.kickrank.common.enums.ApplicationStatus;
import com.kickrank.common.enums.Role;
import com.kickrank.user.entity.RoleApplication;

import java.time.Instant;
import java.util.UUID;

public record RoleApplicationResponse(
        UUID id,
        UserSummary applicant,
        Role requestedRole,
        ApplicationStatus status,
        String motivation,
        Instant createdAt
) {
    public static RoleApplicationResponse from(RoleApplication application) {
        return new RoleApplicationResponse(
                application.getId(),
                UserSummary.from(application.getApplicant()),
                application.getRequestedRole(),
                application.getStatus(),
                application.getMotivation(),
                application.getCreatedAt()
        );
    }
}
