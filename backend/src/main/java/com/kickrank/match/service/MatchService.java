package com.kickrank.match.service;

import com.kickrank.match.dto.CreateMatchRequest;
import com.kickrank.match.dto.MatchResponse;
import com.kickrank.match.dto.ValidateMatchRequest;

import java.util.List;
import java.util.UUID;

public interface MatchService {
    List<MatchResponse> findAll();
    MatchResponse findById(UUID id);
    MatchResponse create(String organizerEmail, CreateMatchRequest request);
    MatchResponse join(String playerEmail, UUID matchId);
    MatchResponse generateTeams(UUID matchId);
    MatchResponse validateResult(String observerEmail, UUID matchId, ValidateMatchRequest request);
}
