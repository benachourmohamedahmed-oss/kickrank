package com.kickrank.match.dto;

import com.kickrank.common.enums.TeamCode;
import com.kickrank.match.entity.Participation;
import com.kickrank.user.dto.UserSummary;

import java.util.UUID;

public record ParticipationResponse(
        UUID id,
        UserSummary player,
        TeamCode teamCode,
        Integer goals,
        Integer assists,
        Double observerRating
) {
    public static ParticipationResponse from(Participation participation) {
        return new ParticipationResponse(
                participation.getId(),
                UserSummary.from(participation.getPlayer()),
                participation.getTeamCode(),
                participation.getGoals(),
                participation.getAssists(),
                participation.getObserverRating()
        );
    }
}
