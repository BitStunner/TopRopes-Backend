package com.topropes.backend.auth.repo;

import com.topropes.backend.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);

    void deleteByExpiresAtBefore(OffsetDateTime dateTime);
}
