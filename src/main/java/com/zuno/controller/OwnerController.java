package com.zuno.controller;

import com.zuno.dto.*;
import com.zuno.model.Listing;
import com.zuno.model.Room;
import com.zuno.service.ListingService;
import com.zuno.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final ListingService listingService;
    private final RoomService roomService;

    /**
     * T-08: Create a new PG listing
     * POST /api/owner/listings
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

    /**
     * T-15: Add a room to a listing
     * POST /api/owner/listings/{listingId}/rooms
     */
    @PostMapping("/listings/{listingId}/rooms")
    public ResponseEntity<ApiResponse<RoomResponse>> addRoom(
            @PathVariable UUID listingId,
            @Valid @RequestBody CreateRoomRequest request) {

        Room room = roomService.addRoom(listingId, request);
        RoomResponse response = RoomResponse.from(room);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Room added successfully"));
    }

    /**
     * Get all rooms for a listing
     * GET /api/owner/listings/{listingId}/rooms
     */
    @GetMapping("/listings/{listingId}/rooms")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getRooms(@PathVariable UUID listingId) {
        List<RoomResponse> rooms = roomService.getRoomsByListing(listingId).stream()
                .map(RoomResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(rooms));
    }
}
