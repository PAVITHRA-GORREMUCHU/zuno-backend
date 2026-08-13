package com.zuno.service;

import com.zuno.dto.CreateRoomRequest;
import com.zuno.exception.ResourceNotFoundException;
import com.zuno.model.Listing;
import com.zuno.model.Room;
import com.zuno.repository.ListingRepository;
import com.zuno.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ListingRepository listingRepository;
    private final ListingService listingService;

    /**
     * T-15: Add a room to a listing
     */
    @Transactional
    public Room addRoom(UUID listingId, CreateRoomRequest request) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + listingId));

        Room room = Room.builder()
                .listing(listing)
                .roomLabel(request.getRoomLabel())
                .roomType(request.getRoomType())
                .capacity(request.getCapacity())
                .occupiedCount(0)
                .build();

        Room savedRoom = roomRepository.save(room);

        // T-16: Recalculate vacancy after adding room
        listingService.recalculateVacancy(listingId);

        return savedRoom;
    }

    /**
     * Get all rooms for a listing
     */
    public List<Room> getRoomsByListing(UUID listingId) {
        // Verify listing exists
        if (!listingRepository.existsById(listingId)) {
            throw new ResourceNotFoundException("Listing not found with id: " + listingId);
        }
        return roomRepository.findByListingId(listingId);
    }
}
