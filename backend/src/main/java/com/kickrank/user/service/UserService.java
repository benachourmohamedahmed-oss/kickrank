package com.kickrank.user.service;

import com.kickrank.user.dto.RoleApplicationRequest;
import com.kickrank.user.dto.RoleApplicationResponse;
import com.kickrank.user.dto.UserSummary;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserSummary currentUser(String email);
    List<UserSummary> leaderboard();
    RoleApplicationResponse applyForRole(String email, RoleApplicationRequest request);
    List<RoleApplicationResponse> pendingApplications();
    RoleApplicationResponse approveApplication(UUID applicationId);
    RoleApplicationResponse rejectApplication(UUID applicationId);
}
