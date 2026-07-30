package com.topropes.backend.catalog.repo;

import com.topropes.backend.catalog.model.FeudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface FeudRepository extends JpaRepository<FeudEntity, UUID>, JpaSpecificationExecutor<FeudEntity> {
    Optional<FeudEntity> findBySlug(String slug);
}
