package com.topropes.backend.catalog.service;

import com.topropes.backend.catalog.dto.CatalogImportResultDto;
import com.topropes.backend.catalog.dto.EventDto;
import com.topropes.backend.catalog.dto.EventUpsertRequest;
import com.topropes.backend.catalog.dto.FeudDto;
import com.topropes.backend.catalog.dto.FeudParticipantDto;
import com.topropes.backend.catalog.dto.FeudUpsertRequest;
import com.topropes.backend.catalog.dto.MatchDto;
import com.topropes.backend.catalog.dto.MatchUpsertRequest;
import com.topropes.backend.catalog.dto.PromotionDto;
import com.topropes.backend.catalog.dto.WrestlerDto;
import com.topropes.backend.catalog.dto.WrestlerUpsertRequest;
import com.topropes.backend.catalog.model.EventEntity;
import com.topropes.backend.catalog.model.FeudEntity;
import com.topropes.backend.catalog.model.MatchCardEntity;
import com.topropes.backend.catalog.model.PromotionEntity;
import com.topropes.backend.catalog.model.WrestlerEntity;
import com.topropes.backend.catalog.repo.EventRepository;
import com.topropes.backend.catalog.repo.FeudRepository;
import com.topropes.backend.catalog.repo.MatchCardRepository;
import com.topropes.backend.catalog.repo.PromotionRepository;
import com.topropes.backend.catalog.repo.WrestlerRepository;
import com.topropes.backend.common.JsonHelper;
import com.topropes.backend.common.api.PageMeta;
import com.topropes.backend.common.api.PagedResponse;
import com.topropes.backend.common.error.ApiException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CatalogService {

    private final PromotionRepository promotionRepository;
    private final WrestlerRepository wrestlerRepository;
    private final EventRepository eventRepository;
    private final MatchCardRepository matchCardRepository;
    private final FeudRepository feudRepository;
    private final JsonHelper jsonHelper;

    public CatalogService(
            PromotionRepository promotionRepository,
            WrestlerRepository wrestlerRepository,
            EventRepository eventRepository,
            MatchCardRepository matchCardRepository,
            FeudRepository feudRepository,
            JsonHelper jsonHelper
    ) {
        this.promotionRepository = promotionRepository;
        this.wrestlerRepository = wrestlerRepository;
        this.eventRepository = eventRepository;
        this.matchCardRepository = matchCardRepository;
        this.feudRepository = feudRepository;
        this.jsonHelper = jsonHelper;
    }

    public Map<String, List<PromotionDto>> listPromotions() {
        List<PromotionDto> items = promotionRepository.findAll().stream()
                .map(p -> new PromotionDto(p.getCode(), p.getName()))
                .toList();
        return Map.of("items", items);
    }

    public PagedResponse<WrestlerDto> listWrestlers(String promotion, String q, int page, int size) {
        Specification<WrestlerEntity> spec = (root, query, cb) -> cb.conjunction();
        if (promotion != null && !promotion.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("promotionCode")), promotion.toLowerCase()));
        }
        if (q != null && !q.isBlank()) {
            String s = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), s),
                    cb.like(cb.lower(root.get("tag")), s)
            ));
        }
        Page<WrestlerEntity> result = wrestlerRepository.findAll(spec, PageRequest.of(page, size));
        return new PagedResponse<>(result.getContent().stream().map(this::toWrestler).toList(),
                new PageMeta(result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages()));
    }

    public WrestlerDto getWrestlerBySlug(String slug) {
        WrestlerEntity e = wrestlerRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Wrestler not found."));
        return toWrestler(e);
    }

    public PagedResponse<EventDto> listEvents(String promotion, String type, Integer year, String q, int page, int size) {
        Specification<EventEntity> spec = (root, query, cb) -> cb.conjunction();
        if (promotion != null && !promotion.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("promotionCode")), promotion.toLowerCase()));
        }
        if (type != null && !type.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }
        if (year != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("eventDate"),
                    java.time.LocalDate.of(year, 1, 1), java.time.LocalDate.of(year, 12, 31)));
        }
        if (q != null && !q.isBlank()) {
            String s = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), s),
                    cb.like(cb.lower(root.get("venue")), s),
                    cb.like(cb.lower(root.get("location")), s)
            ));
        }

        Page<EventEntity> result = eventRepository.findAll(spec, PageRequest.of(page, size));
        return new PagedResponse<>(result.getContent().stream().map(this::toEvent).toList(),
                new PageMeta(result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages()));
    }

    public EventDto getEventBySlug(String slug) {
        EventEntity e = eventRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Event not found."));
        return toEvent(e);
    }

    public PagedResponse<MatchDto> listMatches(String promotion, String eventSlug, String feudSlug, String q, int page, int size) {
        Specification<MatchCardEntity> spec = (root, query, cb) -> cb.conjunction();
        if (promotion != null && !promotion.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("promotion")), promotion.toLowerCase()));
        }
        if (eventSlug != null && !eventSlug.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("eventSlug"), eventSlug));
        }
        if (feudSlug != null && !feudSlug.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("feudSlug"), feudSlug));
        }
        if (q != null && !q.isBlank()) {
            String s = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), s));
        }

        Page<MatchCardEntity> result = matchCardRepository.findAll(spec, PageRequest.of(page, size));
        return new PagedResponse<>(result.getContent().stream().map(this::toMatch).toList(),
                new PageMeta(result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages()));
    }

    public MatchDto getMatchBySlug(String slug) {
        MatchCardEntity e = matchCardRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Match not found."));
        return toMatch(e);
    }

    public PagedResponse<FeudDto> listFeuds(String q, int page, int size) {
        Specification<FeudEntity> spec = (root, query, cb) -> cb.conjunction();
        if (q != null && !q.isBlank()) {
            String s = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("aName")), s),
                    cb.like(cb.lower(root.get("bName")), s),
                    cb.like(cb.lower(root.get("status")), s)
            ));
        }

        Page<FeudEntity> result = feudRepository.findAll(spec, PageRequest.of(page, size));
        return new PagedResponse<>(result.getContent().stream().map(this::toFeud).toList(),
                new PageMeta(result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages()));
    }

    public FeudDto getFeudBySlug(String slug) {
        FeudEntity e = feudRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Feud not found."));
        return toFeud(e);
    }

    @Transactional
    public EventDto upsertEvent(String slug, EventUpsertRequest request) {
        EventEntity entity = eventRepository.findBySlug(slug).orElseGet(EventEntity::new);
        entity.setSlug(slug);
        entity.setName(request.name());
        entity.setPromotionCode(request.promotion());
        entity.setType(request.type());
        entity.setEventDate(request.eventDate());
        entity.setDisplayDate(request.displayDate());
        entity.setVenue(request.venue());
        entity.setLocation(request.location());
        entity.setBroadcastType(request.broadcastType());
        entity.setBroadcastDate(request.broadcastDate());
        entity.setNetwork(request.network());
        entity.setCommentary(request.commentary());
        EventEntity saved = eventRepository.save(entity);
        return toEvent(saved);
    }

    @Transactional
    public WrestlerDto upsertWrestler(String slug, WrestlerUpsertRequest request) {
        WrestlerEntity entity = wrestlerRepository.findBySlug(slug).orElseGet(WrestlerEntity::new);
        entity.setSlug(slug);
        entity.setName(request.name());
        entity.setPromotionCode(request.promotion());
        entity.setTag(request.tag());
        entity.setInitials(request.initials());
        entity.setHeight(request.height());
        entity.setWeight(request.weight());
        entity.setHometown(request.hometown());
        entity.setFinisher(request.finisher());
        entity.setBio(request.bio());
        entity.setImageUrl(request.imageUrl());
        return toWrestler(wrestlerRepository.save(entity));
    }

    @Transactional
    public void deleteEvent(String slug) {
        eventRepository.findBySlug(slug).ifPresent(eventRepository::delete);
    }

    @Transactional
    public void deleteMatch(String slug) {
        matchCardRepository.findBySlug(slug).ifPresent(matchCardRepository::delete);
    }

    @Transactional
    public void deleteWrestler(String slug) {
        wrestlerRepository.findBySlug(slug).ifPresent(wrestlerRepository::delete);
    }

    @Transactional
    public MatchDto upsertMatch(String slug, MatchUpsertRequest request) {
        MatchCardEntity entity = matchCardRepository.findBySlug(slug).orElseGet(MatchCardEntity::new);
        entity.setSlug(slug);
        entity.setName(request.name());
        entity.setEventSlug(request.eventSlug());
        entity.setEventLabel(request.eventSlug());
        entity.setFeudSlug(request.feudSlug());
        entity.setDateLabel(request.date());
        entity.setPromotion(request.promotion());
        entity.setType(request.type());
        entity.setStipulation(request.stipulation());
        entity.setDuration(request.duration());
        entity.setWinner(request.winner());
        entity.setCard(request.card());
        entity.setParticipants(jsonHelper.toJson(request.participants()));
        entity.setSides(jsonHelper.toJson(request.sides() == null ? List.of() : request.sides()));
        entity.setEntrants(jsonHelper.toJson(request.entrants() == null ? List.of() : request.entrants()));
        MatchCardEntity saved = matchCardRepository.save(entity);
        return toMatch(saved);
    }

    @Transactional
    public FeudDto upsertFeud(String slug, FeudUpsertRequest request) {
        FeudEntity entity = feudRepository.findBySlug(slug).orElseGet(FeudEntity::new);
        entity.setSlug(slug);
        entity.setAName(request.a() == null ? null : request.a().name());
        entity.setATag(request.a() == null ? null : request.a().tag());
        entity.setBName(request.b() == null ? null : request.b().name());
        entity.setBTag(request.b() == null ? null : request.b().tag());
        entity.setStatus(request.status());
        entity.setHeat((short) request.heat());
        entity.setUpdatedLabel(request.updated());
        FeudEntity saved = feudRepository.save(entity);
        return toFeud(saved);
    }

    public CatalogImportResultDto importCatalog(Object payload) {
        return new CatalogImportResultDto(UUID.randomUUID(), "accepted", "Catalog import accepted.");
    }

    private WrestlerDto toWrestler(WrestlerEntity e) {
        return new WrestlerDto(e.getSlug(), e.getName(), e.getPromotionCode(), e.getTag(), e.getInitials(), e.getImageUrl(),
            e.getHeight(), e.getWeight(), e.getHometown(), e.getFinisher(), e.getBio());
    }

    private EventDto toEvent(EventEntity e) {
        List<String> matchSlugs = matchCardRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("eventSlug"), e.getSlug())
        ).stream().map(MatchCardEntity::getSlug).toList();

        return new EventDto(
                e.getSlug(),
                e.getName(),
                e.getPromotionCode(),
                e.getType(),
                e.getEventDate(),
                e.getDisplayDate(),
                e.getVenue(),
                e.getLocation(),
                e.getBroadcastType(),
                e.getBroadcastDate(),
                e.getNetwork(),
                e.getCommentary(),
                matchSlugs
        );
    }

    private MatchDto toMatch(MatchCardEntity e) {
        return new MatchDto(
                e.getSlug(),
                e.getName(),
                e.getEventLabel(),
                e.getEventSlug(),
                e.getDateLabel(),
                e.getPromotion(),
                e.getType(),
                e.getStipulation(),
                e.getDuration(),
                e.getWinner(),
                e.getCard(),
                jsonHelper.readStringList(e.getParticipants()),
                jsonHelper.readObjectList(e.getSides()),
                jsonHelper.readStringList(e.getEntrants()),
                e.getFeudSlug()
        );
    }

    private FeudDto toFeud(FeudEntity e) {
        return new FeudDto(
                e.getSlug(),
                new FeudParticipantDto(e.getAName(), e.getATag()),
                new FeudParticipantDto(e.getBName(), e.getBTag()),
                e.getStatus(),
                e.getHeat() == null ? 0 : e.getHeat(),
                e.getUpdatedLabel()
        );
    }
}
