package com.topropes.backend.community.repo;

import com.topropes.backend.community.model.FeudDossierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface FeudDossierRepository extends JpaRepository<FeudDossierEntity, UUID> {
    Optional<FeudDossierEntity> findByUserIdAndFeudSlug(UUID userId, String feudSlug);

    List<FeudDossierEntity> findByUserId(UUID userId);

    void deleteByUserIdAndFeudSlug(UUID userId, String feudSlug);
}
