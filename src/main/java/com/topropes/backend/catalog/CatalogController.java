package com.topropes.backend.catalog;

import com.topropes.backend.catalog.dto.CatalogImportResultDto;
import com.topropes.backend.catalog.dto.EventDto;
import com.topropes.backend.catalog.dto.EventUpsertRequest;
import com.topropes.backend.catalog.dto.FeudDto;
import com.topropes.backend.catalog.dto.FeudUpsertRequest;
import com.topropes.backend.catalog.dto.MatchDto;
import com.topropes.backend.catalog.dto.MatchUpsertRequest;
import com.topropes.backend.catalog.dto.PromotionDto;
import com.topropes.backend.catalog.dto.WrestlerDto;
import com.topropes.backend.catalog.dto.WrestlerUpsertRequest;
import com.topropes.backend.catalog.service.CatalogService;
import com.topropes.backend.common.api.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/catalog/promotions")
    public Map<String, List<PromotionDto>> listPromotions() {
        return catalogService.listPromotions();
    }

    @GetMapping("/catalog/wrestlers")
    public PagedResponse<WrestlerDto> listWrestlers(
            @RequestParam(required = false) String promotion,
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        return catalogService.listWrestlers(promotion, query, page, size);
    }

    @GetMapping("/catalog/wrestlers/{slug}")
    public WrestlerDto getWrestler(@PathVariable String slug) {
        return catalogService.getWrestlerBySlug(slug);
    }

    @GetMapping("/catalog/events")
    public PagedResponse<EventDto> listEvents(
            @RequestParam(required = false) String promotion,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        return catalogService.listEvents(promotion, type, year, query, page, size);
    }

    @GetMapping("/catalog/events/{slug}")
    public EventDto getEvent(@PathVariable String slug) {
        return catalogService.getEventBySlug(slug);
    }

    @GetMapping("/catalog/matches")
    public PagedResponse<MatchDto> listMatches(
            @RequestParam(required = false) String promotion,
            @RequestParam(required = false) String eventSlug,
            @RequestParam(required = false) String feudSlug,
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        return catalogService.listMatches(promotion, eventSlug, feudSlug, query, page, size);
    }

    @GetMapping("/catalog/matches/{slug}")
    public MatchDto getMatch(@PathVariable String slug) {
        return catalogService.getMatchBySlug(slug);
    }

    @GetMapping("/catalog/feuds")
    public PagedResponse<FeudDto> listFeuds(
            @RequestParam(required = false, name = "q") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        return catalogService.listFeuds(query, page, size);
    }

    @GetMapping("/catalog/feuds/{slug}")
    public FeudDto getFeud(@PathVariable String slug) {
        return catalogService.getFeudBySlug(slug);
    }

    @PostMapping("/admin/catalog/import")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public CatalogImportResultDto importCatalog(@RequestBody Map<String, Object> payload) {
        return catalogService.importCatalog(payload);
    }

    @PutMapping("/admin/catalog/events/{slug}")
    @PreAuthorize("hasRole('ADMIN')")
    public EventDto upsertEvent(@PathVariable String slug, @Valid @RequestBody EventUpsertRequest request) {
        return catalogService.upsertEvent(slug, request);
    }

    @PutMapping("/admin/catalog/matches/{slug}")
    @PreAuthorize("hasRole('ADMIN')")
    public MatchDto upsertMatch(@PathVariable String slug, @Valid @RequestBody MatchUpsertRequest request) {
        return catalogService.upsertMatch(slug, request);
    }

    @PutMapping("/admin/catalog/wrestlers/{slug}")
    @PreAuthorize("hasRole('ADMIN')")
    public WrestlerDto upsertWrestler(@PathVariable String slug, @Valid @RequestBody WrestlerUpsertRequest request) {
        return catalogService.upsertWrestler(slug, request);
    }

    @DeleteMapping("/admin/catalog/events/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEvent(@PathVariable String slug) {
        catalogService.deleteEvent(slug);
    }

    @DeleteMapping("/admin/catalog/matches/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMatch(@PathVariable String slug) {
        catalogService.deleteMatch(slug);
    }

    @DeleteMapping("/admin/catalog/wrestlers/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWrestler(@PathVariable String slug) {
        catalogService.deleteWrestler(slug);
    }

    @PutMapping("/admin/catalog/feuds/{slug}")
    @PreAuthorize("hasRole('ADMIN')")
    public FeudDto upsertFeud(@PathVariable String slug, @Valid @RequestBody FeudUpsertRequest request) {
        return catalogService.upsertFeud(slug, request);
    }
}
