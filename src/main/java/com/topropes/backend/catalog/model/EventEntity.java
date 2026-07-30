package com.topropes.backend.catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "event")
@Getter
@Setter
public class EventEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "promotion_code", nullable = false, length = 16)
    private String promotionCode;

    @Column(nullable = false, length = 32)
    private String type;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "display_date", length = 64)
    private String displayDate;

    @Column(length = 200)
    private String venue;

    @Column(length = 200)
    private String location;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
