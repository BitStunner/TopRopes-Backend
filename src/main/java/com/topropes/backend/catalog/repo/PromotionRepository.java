package com.topropes.backend.catalog.repo;

import com.topropes.backend.catalog.model.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<PromotionEntity, UUID> {
    Optional<PromotionEntity> findByCodeIgnoreCase(String code);
}
