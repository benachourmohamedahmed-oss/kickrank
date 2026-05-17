package com.kickrank.match.controller;

import com.kickrank.match.dto.CreateMatchRequest;
import com.kickrank.match.dto.MatchResponse;
import com.kickrank.match.dto.ValidateMatchRequest;
import com.kickrank.match.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping
    List<MatchResponse> findAll() {
        return matchService.findAll();
    }

    @GetMapping("/{id}")
    MatchResponse findById(@PathVariable UUID id) {
        return matchService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    MatchResponse create(Principal principal, @Valid @RequestBody CreateMatchRequest request) {
        return matchService.create(principal.getName(), request);
    }

    @PostMapping("/{id}/join")
    MatchResponse join(Principal principal, @PathVariable UUID id) {
        return matchService.join(principal.getName(), id);
    }

    @PostMapping("/{id}/generate-teams")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    MatchResponse generateTeams(@PathVariable UUID id) {
        return matchService.generateTeams(id);
    }

    @PostMapping("/{id}/validate")
    @PreAuthorize("hasAnyRole('OBSERVER','ADMIN')")
    MatchResponse validate(Principal principal, @PathVariable UUID id, @Valid @RequestBody ValidateMatchRequest request) {
        return matchService.validateResult(principal.getName(), id, request);
    }
}
