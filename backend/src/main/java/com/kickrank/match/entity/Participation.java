package com.kickrank.match.entity;

import com.kickrank.common.enums.TeamCode;
import com.kickrank.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "participations", uniqueConstraints = {
        @UniqueConstraint(name = "uk_match_player", columnNames = {"match_id", "player_id"})
})
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User player;

    @Enumerated(EnumType.STRING)
    private TeamCode teamCode;

    private Integer goals = 0;
    private Integer assists = 0;
    private Double observerRating;
}
