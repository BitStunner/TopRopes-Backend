package com.topropes.backend.community.model;

import com.topropes.backend.auth.model.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "match_rating")
@Getter
@Setter
public class MatchRatingEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "match_slug", nullable = false, length = 120)
    private String matchSlug;

    @Column(nullable = false)
    private Integer crowd;

    @Column(nullable = false)
    private Integer story;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(nullable = false)
    private Integer technique;

    @Column(name = "personal_stars", nullable = false)
    private Double personalStars;

    @Column(nullable = false)
    private String review;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String notes;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
