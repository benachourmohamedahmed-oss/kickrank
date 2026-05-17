package com.kickrank.match.dto;

import com.kickrank.common.enums.MatchFormat;
import com.kickrank.common.enums.MatchStatus;
import com.kickrank.common.enums.MatchType;
import com.kickrank.match.entity.Match;
import com.kickrank.match.entity.Participation;
import com.kickrank.user.dto.UserSummary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MatchResponse(
        UUID id,
        String title,
        String location,
        LocalDateTime scheduledAt,
        MatchType type,
        MatchFormat format,
        MatchStatus status,
        UserSummary organizer,
        UserSummary observer,
        Integer teamAScore,
        Integer teamBScore,
        boolean validated,
        int capacity,
        List<ParticipationResponse> participants
) {
    public static MatchResponse from(Match match, List<Participation> participations) {
        return new MatchResponse(
                match.getId(),
                match.getTitle(),
                match.getLocation(),
                match.getScheduledAt(),
                match.getType(),
                match.getFormat(),
                match.getStatus(),
                UserSummary.from(match.getOrganizer()),
                match.getObserver() == null ? null : UserSummary.from(match.getObserver()),
                match.getTeamAScore(),
                match.getTeamBScore(),
                match.isValidated(),
                match.getFormat().capacity(),
                participations.stream().map(ParticipationResponse::from).toList()
        );
    }
}
