package com.topropes.backend.catalog.repo;

import com.topropes.backend.catalog.model.WrestlerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface WrestlerRepository extends JpaRepository<WrestlerEntity, UUID>, JpaSpecificationExecutor<WrestlerEntity> {
    Optional<WrestlerEntity> findBySlug(String slug);
}
