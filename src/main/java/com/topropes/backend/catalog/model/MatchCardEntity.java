package com.topropes.backend.catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "match_card")
@Getter
@Setter
public class MatchCardEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(nullable = false, length = 220)
    private String name;

    @Column(name = "event_label", nullable = false, length = 200)
    private String eventLabel;

    @Column(name = "event_slug", length = 120)
    private String eventSlug;

    @Column(name = "feud_slug", length = 120)
    private String feudSlug;

    @Column(name = "date_label", nullable = false, length = 120)
    private String dateLabel;

    @Column(nullable = false, length = 16)
    private String promotion;

    @Column(nullable = false, length = 120)
    private String type;

    @Column(length = 240)
    private String stipulation;

    @Column(length = 32)
    private String duration;

    @Column(length = 160)
    private String winner;

    @Column(length = 120)
    private String card;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String participants;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String sides;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String entrants;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
