package com.zuno.controller;

import com.zuno.dto.ApiResponse;
import com.zuno.dto.ListingResponse;
import com.zuno.dto.PaginationInfo;
import com.zuno.model.Listing;
import com.zuno.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    /**
     * T-09: Get all active listings with pagination
     * T-11: Filter by area, budget, gender (all optional)
     *
     * GET /api/listings?page=0&size=10&area=TNGOS&budget_max=10000&gender=MALE
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ListingResponse>>> getListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String area,
            @RequestParam(name = "budget_max", required = false) Integer budgetMax,
            @RequestParam(required = false) String gender) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Parse gender if provided
        Listing.GenderPreference genderPref = null;
        if (gender != null && !gender.isBlank()) {
            try {
                genderPref = Listing.GenderPreference.valueOf(gender.toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // Invalid gender value — ignore filter
            }
        }

        // If any filter is provided, use search; otherwise get all
        Page<Listing> listings;
        if (area != null || budgetMax != null || genderPref != null) {
            listings = listingService.searchListings(area, budgetMax, genderPref, pageable);
        } else {
            listings = listingService.getAllActiveListings(pageable);
        }

        List<ListingResponse> data = listings.getContent().stream()
                .map(ListingResponse::from)
                .toList();

        ApiResponse<List<ListingResponse>> response = ApiResponse.<List<ListingResponse>>builder()
                .success(true)
                .data(data)
                .pagination(PaginationInfo.from(listings))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * T-10: Get single listing by ID
     * GET /api/listings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ListingResponse>> getListingById(@PathVariable UUID id) {
        Listing listing = listingService.getListingById(id);
        ListingResponse data = ListingResponse.from(listing);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * Stats endpoint for homepage
     * GET /api/listings/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        Map<String, Long> stats = Map.of(
                "totalListings", listingService.getActiveListingCount(),
                "totalVacantBeds", listingService.getTotalVacantBeds()
        );
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
