package com.kickrank.match.entity;

import com.kickrank.common.enums.MatchFormat;
import com.kickrank.common.enums.MatchStatus;
import com.kickrank.common.enums.MatchType;
import com.kickrank.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 140)
    private String title;

    @Column(nullable = false, length = 180)
    private String location;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchFormat format;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.WAITING;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User organizer;

    @ManyToOne(fetch = FetchType.LAZY)
    private User observer;

    private Integer teamAScore;
    private Integer teamBScore;

    @Column(nullable = false)
    private boolean validated;

    @CreationTimestamp
    private Instant createdAt;
}
