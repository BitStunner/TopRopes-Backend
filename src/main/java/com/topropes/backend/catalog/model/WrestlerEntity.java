package com.topropes.backend.catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wrestler")
@Getter
@Setter
public class WrestlerEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(name = "promotion_code", nullable = false, length = 16)
    private String promotionCode;

    @Column(length = 200)
    private String tag;

    @Column(length = 8)
    private String initials;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(length = 64)
    private String height;

    @Column(length = 64)
    private String weight;

    @Column(length = 160)
    private String hometown;

    @Column(length = 200)
    private String finisher;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
