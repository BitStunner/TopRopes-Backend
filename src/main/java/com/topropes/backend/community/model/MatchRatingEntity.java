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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.math.BigDecimal;
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
    private Short crowd;

    @Column(nullable = false)
    private Short story;

    @Column(nullable = false)
    private Short difficulty;

    @Column(nullable = false)
    private Short technique;

    @Column(name = "personal_stars", nullable = false)
    private BigDecimal personalStars;

    @Column(nullable = false)
    private String review;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String notes;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
}
