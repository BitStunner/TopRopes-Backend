package com.topropes.backend.community.repo;

import com.topropes.backend.community.model.MatchRatingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MatchRatingRepository extends JpaRepository<MatchRatingEntity, UUID> {
    Page<MatchRatingEntity> findByUserId(UUID userId, Pageable pageable);

    Optional<MatchRatingEntity> findByUserIdAndMatchSlug(UUID userId, String matchSlug);

    void deleteByUserIdAndMatchSlug(UUID userId, String matchSlug);
}
