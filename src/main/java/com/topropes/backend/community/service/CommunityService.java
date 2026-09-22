package com.topropes.backend.community.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topropes.backend.auth.model.AppUser;
import com.topropes.backend.auth.repo.AppUserRepository;
import com.topropes.backend.auth.security.AppUserPrincipal;
import com.topropes.backend.common.JsonHelper;
import com.topropes.backend.common.api.PageMeta;
import com.topropes.backend.common.api.PagedResponse;
import com.topropes.backend.common.error.ApiException;
import com.topropes.backend.community.dto.FeudDossierDto;
import com.topropes.backend.community.dto.FeudDossierUpsertRequest;
import com.topropes.backend.community.dto.FeudPromoDto;
import com.topropes.backend.community.dto.FeudPromoUpsertRequest;
import com.topropes.backend.community.dto.KeyDevelopmentDto;
import com.topropes.backend.community.dto.MatchRatingDto;
import com.topropes.backend.community.dto.MatchRatingUpsertRequest;
import com.topropes.backend.community.model.FeudDossierEntity;
import com.topropes.backend.community.model.FeudPromoEntity;
import com.topropes.backend.community.model.MatchRatingEntity;
import com.topropes.backend.community.repo.FeudDossierRepository;
import com.topropes.backend.community.repo.FeudPromoRepository;
import com.topropes.backend.community.repo.MatchRatingRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.math.BigDecimal;

@Service
public class CommunityService {

    private final MatchRatingRepository matchRatingRepository;
    private final FeudDossierRepository feudDossierRepository;
    private final FeudPromoRepository feudPromoRepository;
    private final AppUserRepository appUserRepository;
    private final JsonHelper jsonHelper;
    private final ObjectMapper objectMapper;

    public CommunityService(
            MatchRatingRepository matchRatingRepository,
            FeudDossierRepository feudDossierRepository,
            FeudPromoRepository feudPromoRepository,
            AppUserRepository appUserRepository,
            JsonHelper jsonHelper,
            ObjectMapper objectMapper
    ) {
        this.matchRatingRepository = matchRatingRepository;
        this.feudDossierRepository = feudDossierRepository;
        this.feudPromoRepository = feudPromoRepository;
        this.appUserRepository = appUserRepository;
        this.jsonHelper = jsonHelper;
        this.objectMapper = objectMapper;
    }

    public PagedResponse<MatchRatingDto> listMyRatings(AppUserPrincipal principal, int page, int size) {
        Page<MatchRatingEntity> result = matchRatingRepository.findByUserId(principal.id(), PageRequest.of(page, size));
        return new PagedResponse<>(
                result.getContent().stream().map(this::toRatingDto).toList(),
                new PageMeta(result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages())
        );
    }

    @Transactional
    public MatchRatingDto upsertMyRating(AppUserPrincipal principal, String matchSlug, MatchRatingUpsertRequest request) {
        MatchRatingEntity entity = matchRatingRepository.findByUserIdAndMatchSlug(principal.id(), matchSlug)
                .orElseGet(MatchRatingEntity::new);
        entity.setUser(loadUser(principal.id()));
        entity.setMatchSlug(matchSlug);
        entity.setCrowd((short) request.crowd());
        entity.setStory((short) request.story());
        entity.setDifficulty((short) request.difficulty());
        entity.setTechnique((short) request.technique());
        entity.setPersonalStars(BigDecimal.valueOf(request.personalStars()));
        entity.setReview(request.review() == null ? "" : request.review());
        entity.setNotes(jsonHelper.toJson(request.notes()));
        MatchRatingEntity saved = matchRatingRepository.saveAndFlush(entity);
        return toRatingDto(saved);
    }

    @Transactional
    public void deleteMyRating(AppUserPrincipal principal, String matchSlug) {
        matchRatingRepository.deleteByUserIdAndMatchSlug(principal.id(), matchSlug);
    }

    public FeudDossierDto getMyDossier(AppUserPrincipal principal, String feudSlug) {
        FeudDossierEntity entity = feudDossierRepository.findByUserIdAndFeudSlug(principal.id(), feudSlug)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Dossier not found."));
        return toDossierDto(entity);
    }

    public Map<String, List<FeudDossierDto>> listMyDossiers(AppUserPrincipal principal) {
        List<FeudDossierDto> items = feudDossierRepository.findByUserId(principal.id()).stream()
                .map(this::toDossierDto).toList();
        return Map.of("items", items);
    }

    @Transactional
    public FeudDossierDto upsertMyDossier(AppUserPrincipal principal, String feudSlug, FeudDossierUpsertRequest request) {
        FeudDossierEntity entity = feudDossierRepository.findByUserIdAndFeudSlug(principal.id(), feudSlug)
                .orElseGet(FeudDossierEntity::new);

        entity.setUser(loadUser(principal.id()));
        entity.setFeudSlug(feudSlug);
        entity.setParticipants(request.participants());
        entity.setCause(request.cause());
        entity.setMotivationA(request.motivationA());
        entity.setMotivationB(request.motivationB());
        entity.setBackground(request.background());
        entity.setStakes(request.stakes());
        entity.setCurrentStatus(request.currentStatus());
        entity.setResolution(request.resolution());
        entity.setKeyDevelopments(jsonHelper.toJson(request.keyDevelopments()));
        entity.setHeat((short) request.heat());

        return toDossierDto(feudDossierRepository.saveAndFlush(entity));
    }

    @Transactional
    public void deleteMyDossier(AppUserPrincipal principal, String feudSlug) {
        feudDossierRepository.deleteByUserIdAndFeudSlug(principal.id(), feudSlug);
    }

    public Map<String, List<FeudPromoDto>> listMyPromos(AppUserPrincipal principal, String feudSlug) {
        List<FeudPromoDto> items = feudPromoRepository.findByUserIdAndFeudSlug(principal.id(), feudSlug)
                .stream().map(this::toPromoDto).toList();
        return Map.of("items", items);
    }

    public Map<String, List<FeudPromoDto>> listMyPromos(AppUserPrincipal principal) {
        List<FeudPromoDto> items = feudPromoRepository.findByUserId(principal.id()).stream()
                .map(this::toPromoDto).toList();
        return Map.of("items", items);
    }

    @Transactional
    public FeudPromoDto upsertMyPromo(AppUserPrincipal principal, String feudSlug, String promoSlug, FeudPromoUpsertRequest request) {
        FeudPromoEntity entity = feudPromoRepository.findByUserIdAndFeudSlugAndPromoSlug(principal.id(), feudSlug, promoSlug)
                .orElseGet(FeudPromoEntity::new);

        entity.setUser(loadUser(principal.id()));
        entity.setFeudSlug(feudSlug);
        entity.setPromoSlug(promoSlug);
        entity.setTitle(request.title());
        entity.setSpeaker(request.speaker());
        entity.setPromoDate(request.date());
        entity.setVenue(request.venue());
        entity.setQuote(request.quote());
        entity.setTranscript(request.transcript());
        entity.setImpact(request.impact());

        return toPromoDto(feudPromoRepository.saveAndFlush(entity));
    }

    @Transactional
    public void deleteMyPromo(AppUserPrincipal principal, String feudSlug, String promoSlug) {
        feudPromoRepository.deleteByUserIdAndFeudSlugAndPromoSlug(principal.id(), feudSlug, promoSlug);
    }

    private MatchRatingDto toRatingDto(MatchRatingEntity e) {
        return new MatchRatingDto(
                e.getMatchSlug(),
                e.getCrowd(),
                e.getStory(),
                e.getDifficulty(),
                e.getTechnique(),
                e.getPersonalStars().doubleValue(),
                e.getReview(),
                jsonHelper.readStringMap(e.getNotes()),
                e.getUpdatedAt()
        );
    }

    private FeudDossierDto toDossierDto(FeudDossierEntity e) {
        List<KeyDevelopmentDto> developments;
        try {
            developments = objectMapper.readValue(e.getKeyDevelopments(), new TypeReference<>() {});
        } catch (Exception ex) {
            developments = List.of();
        }

        return new FeudDossierDto(
                e.getFeudSlug(),
                e.getParticipants(),
                e.getCause(),
                e.getMotivationA(),
                e.getMotivationB(),
                e.getBackground(),
                e.getStakes(),
                e.getCurrentStatus(),
                e.getResolution(),
                developments,
                e.getHeat(),
                e.getUpdatedAt()
        );
    }

    private FeudPromoDto toPromoDto(FeudPromoEntity e) {
        return new FeudPromoDto(
                e.getFeudSlug(),
                e.getPromoSlug(),
                e.getTitle(),
                e.getSpeaker(),
                e.getPromoDate(),
                e.getVenue(),
                e.getQuote(),
                e.getTranscript(),
                e.getImpact(),
                e.getUpdatedAt()
        );
    }

    private AppUser loadUser(UUID userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User does not exist."));
    }
}
