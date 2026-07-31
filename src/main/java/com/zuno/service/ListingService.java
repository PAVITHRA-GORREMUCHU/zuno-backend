package com.zuno.service;

import com.zuno.model.Listing;
import com.zuno.model.Room;
import com.zuno.repository.ListingRepository;
import com.zuno.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final RoomRepository roomRepository;

    public Page<Listing> getAllActiveListings(Pageable pageable) {
        return listingRepository.findByIsActiveTrue(pageable);
    }

    public Listing getListingById(UUID id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public Page<Listing> searchListings(String area, Integer maxBudget,
                                         Listing.GenderPreference gender, Pageable pageable) {
        return listingRepository.searchListings(area, maxBudget, gender, pageable);
    }

    /**
     * Recalculates total_beds and vacant_beds for a listing
     * based on the current state of all its rooms.
     * Called after any tenant onboard/offboard action.
     */
    @Transactional
    public void recalculateVacancy(UUID listingId) {
        Listing listing = getListingById(listingId);
        List<Room> rooms = roomRepository.findByListingId(listingId);

        int totalBeds = rooms.stream().mapToInt(Room::getCapacity).sum();
        int occupiedBeds = rooms.stream().mapToInt(Room::getOccupiedCount).sum();

        listing.setTotalBeds(totalBeds);
        listing.setVacantBeds(totalBeds - occupiedBeds);
        listingRepository.save(listing);
    }

    public long getActiveListingCount() {
        return listingRepository.countByIsActiveTrue();
    }

    public long getTotalVacantBeds() {
        Long count = listingRepository.countTotalVacantBeds();
        return count != null ? count : 0;
    }
}
