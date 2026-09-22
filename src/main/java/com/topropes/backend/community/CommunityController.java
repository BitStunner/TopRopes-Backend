package com.topropes.backend.community;

import com.topropes.backend.auth.security.AppUserPrincipal;
import com.topropes.backend.common.api.PagedResponse;
import com.topropes.backend.community.dto.FeudDossierDto;
import com.topropes.backend.community.dto.FeudDossierUpsertRequest;
import com.topropes.backend.community.dto.FeudPromoDto;
import com.topropes.backend.community.dto.FeudPromoUpsertRequest;
import com.topropes.backend.community.dto.MatchRatingDto;
import com.topropes.backend.community.dto.MatchRatingUpsertRequest;
import com.topropes.backend.community.service.CommunityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/me")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/ratings")
    public PagedResponse<MatchRatingDto> listMyRatings(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        return communityService.listMyRatings(principal, page, size);
    }

    @PutMapping("/ratings/{matchSlug}")
    public MatchRatingDto upsertMyRating(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String matchSlug,
            @Valid @RequestBody MatchRatingUpsertRequest request
    ) {
        return communityService.upsertMyRating(principal, matchSlug, request);
    }

    @DeleteMapping("/ratings/{matchSlug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyRating(@AuthenticationPrincipal AppUserPrincipal principal, @PathVariable String matchSlug) {
        communityService.deleteMyRating(principal, matchSlug);
    }

    @GetMapping("/dossiers")
    public Map<String, List<FeudDossierDto>> listMyDossiers(@AuthenticationPrincipal AppUserPrincipal principal) {
        return communityService.listMyDossiers(principal);
    }

    @GetMapping("/feuds/{feudSlug}/dossier")
    public FeudDossierDto getMyDossier(@AuthenticationPrincipal AppUserPrincipal principal, @PathVariable String feudSlug) {
        return communityService.getMyDossier(principal, feudSlug);
    }

    @PutMapping("/feuds/{feudSlug}/dossier")
    public FeudDossierDto upsertMyDossier(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String feudSlug,
            @Valid @RequestBody FeudDossierUpsertRequest request
    ) {
        return communityService.upsertMyDossier(principal, feudSlug, request);
    }

    @DeleteMapping("/feuds/{feudSlug}/dossier")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyDossier(@AuthenticationPrincipal AppUserPrincipal principal, @PathVariable String feudSlug) {
        communityService.deleteMyDossier(principal, feudSlug);
    }

    @GetMapping("/feuds/{feudSlug}/promos")
    public Map<String, List<FeudPromoDto>> listMyPromos(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String feudSlug
    ) {
        return communityService.listMyPromos(principal, feudSlug);
    }

    @GetMapping("/promos")
    public Map<String, List<FeudPromoDto>> listMyPromos(@AuthenticationPrincipal AppUserPrincipal principal) {
        return communityService.listMyPromos(principal);
    }

    @PutMapping("/feuds/{feudSlug}/promos/{promoSlug}")
    public FeudPromoDto upsertMyPromo(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String feudSlug,
            @PathVariable String promoSlug,
            @Valid @RequestBody FeudPromoUpsertRequest request
    ) {
        return communityService.upsertMyPromo(principal, feudSlug, promoSlug, request);
    }

    @DeleteMapping("/feuds/{feudSlug}/promos/{promoSlug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyPromo(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable String feudSlug,
            @PathVariable String promoSlug
    ) {
        communityService.deleteMyPromo(principal, feudSlug, promoSlug);
    }
}
