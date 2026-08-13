package com.zuno.controller;

import com.zuno.dto.ApiResponse;
import com.zuno.dto.CreateListingRequest;
import com.zuno.dto.ListingResponse;
import com.zuno.model.Listing;
import com.zuno.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final ListingService listingService;

    /**
     * T-08: Create a new PG listing
     * POST /api/owner/listings
     *
     * Note: No auth yet (Sprint 4). Uses header X-Owner-Phone as temporary owner identity.
     */
    @PostMapping("/listings")
    public ResponseEntity<ApiResponse<ListingResponse>> createListing(
            @Valid @RequestBody CreateListingRequest request,
            @RequestHeader(value = "X-Owner-Phone", defaultValue = "9014429058") String ownerPhone) {

        Listing listing = listingService.createListing(request, ownerPhone);
        ListingResponse response = ListingResponse.from(listing);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Listing created successfully"));
    }
}
