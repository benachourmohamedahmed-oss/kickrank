package com.kickrank.match.repository;

import com.kickrank.common.enums.MatchStatus;
import com.kickrank.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {
    List<Match> findByStatusOrderByScheduledAtAsc(MatchStatus status);
    List<Match> findAllByOrderByScheduledAtDesc();
}
