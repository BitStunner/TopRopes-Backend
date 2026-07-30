package com.topropes.backend.community.repo;

import com.topropes.backend.community.model.FeudPromoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeudPromoRepository extends JpaRepository<FeudPromoEntity, UUID> {
    List<FeudPromoEntity> findByUserIdAndFeudSlug(UUID userId, String feudSlug);

    Optional<FeudPromoEntity> findByUserIdAndFeudSlugAndPromoSlug(UUID userId, String feudSlug, String promoSlug);

    void deleteByUserIdAndFeudSlugAndPromoSlug(UUID userId, String feudSlug, String promoSlug);
}
