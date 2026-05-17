package com.kickrank.match.service.impl;

import com.kickrank.common.enums.MatchStatus;
import com.kickrank.common.enums.MatchType;
import com.kickrank.common.enums.TeamCode;
import com.kickrank.common.exception.BadRequestException;
import com.kickrank.common.exception.ResourceNotFoundException;
import com.kickrank.match.dto.CreateMatchRequest;
import com.kickrank.match.dto.MatchResponse;
import com.kickrank.match.dto.ValidateMatchRequest;
import com.kickrank.match.entity.Match;
import com.kickrank.match.entity.Participation;
import com.kickrank.match.repository.MatchRepository;
import com.kickrank.match.repository.ParticipationRepository;
import com.kickrank.match.service.MatchService;
import com.kickrank.user.entity.User;
import com.kickrank.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private static final int K_FACTOR = 32;

    private final MatchRepository matchRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> findAll() {
        return matchRepository.findAllByOrderByScheduledAtDesc().stream()
                .map(match -> MatchResponse.from(match, participationRepository.findByMatch(match)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResponse findById(UUID id) {
        var match = findMatch(id);
        return MatchResponse.from(match, participationRepository.findByMatch(match));
    }

    @Override
    @Transactional
    public MatchResponse create(String organizerEmail, CreateMatchRequest request) {
        var organizer = findUserByEmail(organizerEmail);
        if (request.type() == MatchType.RANKED && !organizer.isOrganizerVerified()) {
            throw new BadRequestException("Only verified organizers can create ranked matches");
        }

        User observer = null;
        if (request.type() == MatchType.RANKED) {
            if (request.observerId() == null) {
                throw new BadRequestException("A verified observer is required for ranked matches");
            }
            observer = findUser(request.observerId());
            if (!observer.isObserverVerified()) {
                throw new BadRequestException("Observer must be verified");
            }
        }

        var match = new Match();
        match.setTitle(request.title());
        match.setLocation(request.location());
        match.setScheduledAt(request.scheduledAt());
        match.setType(request.type());
        match.setFormat(request.format());
        match.setOrganizer(organizer);
        match.setObserver(observer);
        return MatchResponse.from(matchRepository.save(match), List.of());
    }

    @Override
    @Transactional
    public MatchResponse join(String playerEmail, UUID matchId) {
        var player = findUserByEmail(playerEmail);
        var match = findMatch(matchId);
        if (match.getStatus() != MatchStatus.WAITING) {
            throw new BadRequestException("Players can only join waiting matches");
        }
        if (participationRepository.existsByMatchAndPlayer(match, player)) {
            throw new BadRequestException("Player already joined this match");
        }
        if (participationRepository.countByMatch(match) >= match.getFormat().capacity()) {
            throw new BadRequestException("Match is full");
        }
        var participation = new Participation();
        participation.setMatch(match);
        participation.setPlayer(player);
        participationRepository.save(participation);

        if (participationRepository.countByMatch(match) == match.getFormat().capacity()) {
            assignBalancedTeams(match);
        }
        return MatchResponse.from(match, participationRepository.findByMatch(match));
    }

    @Override
    @Transactional
    public MatchResponse generateTeams(UUID matchId) {
        var match = findMatch(matchId);
        if (participationRepository.countByMatch(match) != match.getFormat().capacity()) {
            throw new BadRequestException("Match must be full before generating teams");
        }
        assignBalancedTeams(match);
        return MatchResponse.from(match, participationRepository.findByMatch(match));
    }

    @Override
    @Transactional
    public MatchResponse validateResult(String observerEmail, UUID matchId, ValidateMatchRequest request) {
        var observer = findUserByEmail(observerEmail);
        var match = findMatch(matchId);
        if (match.getType() == MatchType.RANKED && (match.getObserver() == null || !match.getObserver().getId().equals(observer.getId()))) {
            throw new BadRequestException("Only the assigned observer can validate this ranked match");
        }
        if (match.getStatus() != MatchStatus.TEAMS_GENERATED && match.getStatus() != MatchStatus.READY && match.getStatus() != MatchStatus.ONGOING) {
            throw new BadRequestException("Match is not ready for validation");
        }

        var participations = participationRepository.findByMatch(match);
        var byPlayerId = participations.stream()
                .collect(Collectors.toMap(p -> p.getPlayer().getId(), p -> p));
        if (request.performances() != null) {
            request.performances().forEach(performance -> {
                var participation = byPlayerId.get(performance.playerId());
                if (participation != null) {
                    participation.setGoals(performance.goals());
                    participation.setAssists(performance.assists());
                    participation.setObserverRating(performance.observerRating());
                }
            });
        }

        match.setTeamAScore(request.teamAScore());
        match.setTeamBScore(request.teamBScore());
        match.setValidated(true);
        match.setStatus(MatchStatus.FINISHED);

        if (match.getType() == MatchType.RANKED) {
            updateElo(match, participations);
            var organizer = match.getOrganizer();
            organizer.setOrganizerLevel(organizer.getOrganizerLevel() + 1);
        }

        return MatchResponse.from(match, participations);
    }

    private void assignBalancedTeams(Match match) {
        var participations = participationRepository.findByMatch(match).stream()
                .sorted(Comparator.comparing((Participation p) -> p.getPlayer().getElo()).reversed())
                .toList();

        for (int i = 0; i < participations.size(); i++) {
            boolean pairToAFirst = (i / 2) % 2 == 0;
            boolean evenIndex = i % 2 == 0;
            participations.get(i).setTeamCode(pairToAFirst == evenIndex ? TeamCode.A : TeamCode.B);
        }
        match.setStatus(MatchStatus.TEAMS_GENERATED);
    }

    private void updateElo(Match match, List<Participation> participations) {
        var teamA = participations.stream().filter(p -> p.getTeamCode() == TeamCode.A).toList();
        var teamB = participations.stream().filter(p -> p.getTeamCode() == TeamCode.B).toList();
        var avgA = teamA.stream().mapToInt(p -> p.getPlayer().getElo()).average().orElse(1000);
        var avgB = teamB.stream().mapToInt(p -> p.getPlayer().getElo()).average().orElse(1000);
        var expectedA = 1.0 / (1.0 + Math.pow(10, (avgB - avgA) / 400.0));
        var actualA = match.getTeamAScore().equals(match.getTeamBScore()) ? 0.5 : match.getTeamAScore() > match.getTeamBScore() ? 1.0 : 0.0;

        applyTeamElo(teamA, expectedA, actualA);
        applyTeamElo(teamB, 1 - expectedA, 1 - actualA);
    }

    private void applyTeamElo(List<Participation> team, double expected, double actual) {
        var avgRating = team.stream().map(Participation::getObserverRating)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(6.0);
        team.forEach(participation -> {
            var rating = participation.getObserverRating() == null ? avgRating : participation.getObserverRating();
            var individualScore = Math.max(-1.0, Math.min(1.0, (rating - avgRating) / 4.0));
            var collectiveDelta = K_FACTOR * (actual - expected) * 0.5;
            var individualDelta = K_FACTOR * individualScore * 0.5;
            var player = participation.getPlayer();
            player.setElo(Math.max(100, (int) Math.round(player.getElo() + collectiveDelta + individualDelta)));
        });
    }

    private Match findMatch(UUID id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
    }

    private User findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
