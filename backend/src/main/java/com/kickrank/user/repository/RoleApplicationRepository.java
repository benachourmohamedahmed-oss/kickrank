package com.kickrank.user.repository;

import com.kickrank.common.enums.ApplicationStatus;
import com.kickrank.user.entity.RoleApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleApplicationRepository extends JpaRepository<RoleApplication, UUID> {
    List<RoleApplication> findByStatusOrderByCreatedAtDesc(ApplicationStatus status);
}
