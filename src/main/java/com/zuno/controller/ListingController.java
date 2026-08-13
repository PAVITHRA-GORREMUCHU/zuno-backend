package com.zuno.controller;

import com.zuno.dto.*;
import com.zuno.model.Listing;
import com.zuno.service.ListingService;
import com.zuno.service.RoomService;
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
    private final RoomService roomService;

    /**
     * T-09: Get all active listings with pagination
     * T-11: Filter by area, budget, gender (all optional)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ListingResponse>>> getListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String area,
            @RequestParam(name = "budget_max", required = false) Integer budgetMax,
            @RequestParam(required = false) String gender) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Listing.GenderPreference genderPref = null;
        if (gender != null && !gender.isBlank()) {
            try {
                genderPref = Listing.GenderPreference.valueOf(gender.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }

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
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ListingResponse>> getListingById(@PathVariable UUID id) {
        Listing listing = listingService.getListingById(id);
        ListingResponse data = ListingResponse.from(listing);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * T-17: Get rooms for a listing (used by detail page for vacancy display)
     * GET /api/listings/{id}/rooms
     */
    @GetMapping("/{id}/rooms")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getListingRooms(@PathVariable UUID id) {
        List<RoomResponse> rooms = roomService.getRoomsByListing(id).stream()
                .map(RoomResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(rooms));
    }

    /**
     * Stats endpoint for homepage
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
