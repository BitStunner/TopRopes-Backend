package com.topropes.backend.catalog.repo;

import com.topropes.backend.catalog.model.MatchCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface MatchCardRepository extends JpaRepository<MatchCardEntity, UUID>, JpaSpecificationExecutor<MatchCardEntity> {
    Optional<MatchCardEntity> findBySlug(String slug);
}
