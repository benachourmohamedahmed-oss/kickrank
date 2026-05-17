package com.kickrank.user.service.impl;

import com.kickrank.common.enums.ApplicationStatus;
import com.kickrank.common.enums.Role;
import com.kickrank.common.exception.BadRequestException;
import com.kickrank.common.exception.ResourceNotFoundException;
import com.kickrank.user.dto.RoleApplicationRequest;
import com.kickrank.user.dto.RoleApplicationResponse;
import com.kickrank.user.dto.UserSummary;
import com.kickrank.user.entity.RoleApplication;
import com.kickrank.user.entity.User;
import com.kickrank.user.repository.RoleApplicationRepository;
import com.kickrank.user.repository.UserRepository;
import com.kickrank.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleApplicationRepository roleApplicationRepository;

    @Override
    @Transactional(readOnly = true)
    public UserSummary currentUser(String email) {
        return UserSummary.from(findByEmail(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummary> leaderboard() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparingInt(User::getElo).reversed())
                .map(UserSummary::from)
                .toList();
    }

    @Override
    @Transactional
    public RoleApplicationResponse applyForRole(String email, RoleApplicationRequest request) {
        if (request.requestedRole() != Role.ORGANIZER && request.requestedRole() != Role.OBSERVER) {
            throw new BadRequestException("Only organizer and observer applications are allowed");
        }
        var application = new RoleApplication();
        application.setApplicant(findByEmail(email));
        application.setRequestedRole(request.requestedRole());
        application.setMotivation(request.motivation());
        return RoleApplicationResponse.from(roleApplicationRepository.save(application));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<RoleApplicationResponse> pendingApplications() {
        return roleApplicationRepository.findByStatusOrderByCreatedAtDesc(ApplicationStatus.PENDING)
                .stream()
                .map(RoleApplicationResponse::from)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleApplicationResponse approveApplication(UUID applicationId) {
        var application = findApplication(applicationId);
        application.setStatus(ApplicationStatus.APPROVED);
        var user = application.getApplicant();
        user.getRoles().add(application.getRequestedRole());
        if (application.getRequestedRole() == Role.ORGANIZER) {
            user.setOrganizerVerified(true);
        }
        if (application.getRequestedRole() == Role.OBSERVER) {
            user.setObserverVerified(true);
        }
        return RoleApplicationResponse.from(application);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public RoleApplicationResponse rejectApplication(UUID applicationId) {
        var application = findApplication(applicationId);
        application.setStatus(ApplicationStatus.REJECTED);
        return RoleApplicationResponse.from(application);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private RoleApplication findApplication(UUID id) {
        return roleApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }
}
