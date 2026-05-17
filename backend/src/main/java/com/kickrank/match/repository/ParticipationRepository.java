package com.kickrank.match.repository;

import com.kickrank.match.entity.Match;
import com.kickrank.match.entity.Participation;
import com.kickrank.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository<Participation, UUID> {
    long countByMatch(Match match);
    boolean existsByMatchAndPlayer(Match match, User player);
    List<Participation> findByMatch(Match match);
    Optional<Participation> findByMatchAndPlayer(Match match, User player);
}
