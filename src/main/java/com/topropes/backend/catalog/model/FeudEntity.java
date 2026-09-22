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
@Table(name = "feud")
@Getter
@Setter
public class FeudEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(name = "a_name", length = 160)
    private String aName;

    @Column(name = "a_tag", length = 200)
    private String aTag;

    @Column(name = "b_name", length = 160)
    private String bName;

    @Column(name = "b_tag", length = 200)
    private String bTag;

    @Column(nullable = false, length = 200)
    private String status;

    @Column(nullable = false)
    private Short heat;

    @Column(name = "updated_label", length = 120)
    private String updatedLabel;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
