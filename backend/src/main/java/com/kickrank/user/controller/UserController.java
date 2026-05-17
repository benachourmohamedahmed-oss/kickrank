package com.kickrank.user.controller;

import com.kickrank.user.dto.RoleApplicationRequest;
import com.kickrank.user.dto.RoleApplicationResponse;
import com.kickrank.user.dto.UserSummary;
import com.kickrank.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/me")
    UserSummary me(Principal principal) {
        return userService.currentUser(principal.getName());
    }

    @GetMapping("/leaderboard")
    List<UserSummary> leaderboard() {
        return userService.leaderboard();
    }

    @PostMapping("/role-applications")
    RoleApplicationResponse apply(Principal principal, @Valid @RequestBody RoleApplicationRequest request) {
        return userService.applyForRole(principal.getName(), request);
    }

    @GetMapping("/admin/role-applications")
    List<RoleApplicationResponse> pendingApplications() {
        return userService.pendingApplications();
    }

    @PatchMapping("/admin/role-applications/{id}/approve")
    RoleApplicationResponse approve(@PathVariable UUID id) {
        return userService.approveApplication(id);
    }

    @PatchMapping("/admin/role-applications/{id}/reject")
    RoleApplicationResponse reject(@PathVariable UUID id) {
        return userService.rejectApplication(id);
    }
}
