package com.topropes.backend.auth.service;

import com.topropes.backend.auth.dto.AuthSessionDto;
import com.topropes.backend.auth.dto.LoginRequest;
import com.topropes.backend.auth.dto.RefreshRequest;
import com.topropes.backend.auth.dto.RegisterRequest;
import com.topropes.backend.auth.dto.TokenPairDto;
import com.topropes.backend.auth.dto.UserProfileDto;
import com.topropes.backend.auth.model.AppUser;
import com.topropes.backend.auth.model.RefreshToken;
import com.topropes.backend.auth.repo.AppUserRepository;
import com.topropes.backend.auth.repo.RefreshTokenRepository;
import com.topropes.backend.auth.security.AppUserPrincipal;
import com.topropes.backend.auth.security.JwtService;
import com.topropes.backend.common.error.ApiException;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthSessionDto register(RegisterRequest request) {
        String username = request.username().trim().toLowerCase();
        if (appUserRepository.existsByUsernameIgnoreCase(username)) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists.");
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_USER");
        AppUser saved = appUserRepository.save(user);

        return issueSession(saved);
    }

    @Transactional
    public AuthSessionDto login(LoginRequest request) {
        AppUser user = appUserRepository.findByUsernameIgnoreCase(request.username().trim())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
        }

        return issueSession(user);
    }

    @Transactional
    public TokenPairDto refresh(RefreshRequest request) {
        String refreshToken = request.refreshToken();
        String refreshHash = hash(refreshToken);

        Claims claims;
        try {
            claims = jwtService.parseClaims(refreshToken);
        } catch (Exception ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token.");
        }

        UUID userId = UUID.fromString(claims.getSubject());
        RefreshToken stored = refreshTokenRepository.findByTokenHashAndRevokedFalse(refreshHash)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid refresh token."));

        if (stored.getExpiresAt().isBefore(OffsetDateTime.now(ZoneOffset.UTC)) || !stored.getUser().getId().equals(userId)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Refresh token expired or invalid.");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        AppUser user = stored.getUser();
        return issueTokens(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }
        String tokenHash = hash(refreshToken);
        refreshTokenRepository.findByTokenHashAndRevokedFalse(tokenHash).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    public UserProfileDto me(AppUserPrincipal principal) {
        return new UserProfileDto(principal.id(), principal.getUsername(), principal.role(),
                appUserRepository.findById(principal.id()).map(AppUser::getCreatedAt).orElse(OffsetDateTime.now()));
    }

    public boolean isUsernameTaken(String username) {
        return appUserRepository.existsByUsernameIgnoreCase(username.trim());
    }

    private AuthSessionDto issueSession(AppUser user) {
        return new AuthSessionDto(
                new UserProfileDto(user.getId(), user.getUsername(), user.getRole(), user.getCreatedAt()),
                issueTokens(user)
        );
    }

    private TokenPairDto issueTokens(AppUser user) {
        String accessToken = jwtService.createAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtService.createRefreshToken(user.getId());

        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setTokenHash(hash(refreshToken));
        rt.setExpiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusSeconds(1209600));
        rt.setRevoked(false);
        refreshTokenRepository.save(rt);

        return new TokenPairDto(accessToken, refreshToken, "Bearer", jwtService.accessTokenTtlSeconds());
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
