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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "feud_promo")
@Getter
@Setter
public class FeudPromoEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "feud_slug", nullable = false, length = 120)
    private String feudSlug;

    @Column(name = "promo_slug", nullable = false, length = 120)
    private String promoSlug;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String speaker;

    @Column(name = "promo_date")
    private LocalDate promoDate;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private String quote;

    @Column(nullable = false)
    private String transcript;

    @Column(nullable = false)
    private String impact;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
