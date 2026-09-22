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
import java.util.UUID;

@Entity
@Table(name = "feud_dossier")
@Getter
@Setter
public class FeudDossierEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "feud_slug", nullable = false, length = 120)
    private String feudSlug;

    @Column(nullable = false)
    private String participants;

    @Column(nullable = false)
    private String cause;

    @Column(name = "motivation_a", nullable = false)
    private String motivationA;

    @Column(name = "motivation_b", nullable = false)
    private String motivationB;

    @Column(nullable = false)
    private String background;

    @Column(nullable = false)
    private String stakes;

    @Column(name = "current_status", nullable = false)
    private String currentStatus;

    @Column(nullable = false)
    private String resolution;

    @Column(name = "key_developments", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String keyDevelopments;

    @Column(nullable = false)
    private Short heat;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
}
