package com.zuno.service;

import com.zuno.dto.CreateListingRequest;
import com.zuno.exception.ResourceNotFoundException;
import com.zuno.model.Listing;
import com.zuno.model.Room;
import com.zuno.model.User;
import com.zuno.repository.ListingRepository;
import com.zuno.repository.RoomRepository;
import com.zuno.repository.UserRepository;
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
    private final UserRepository userRepository;

    /**
     * T-08: Create a new PG listing.
     * For MVP (no auth yet), we auto-create/find an owner user.
     */
    @Transactional
    public Listing createListing(CreateListingRequest request, String ownerPhone) {
        // Find or create owner (temporary until auth is implemented in Sprint 4)
        User owner = userRepository.findByPhone(ownerPhone)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .phone(ownerPhone)
                                .fullName("Owner")
                                .role(User.Role.OWNER)
                                .build()
                ));

        Listing listing = Listing.builder()
                .owner(owner)
                .pgName(request.getPgName())
                .address(request.getAddress())
                .area(request.getArea())
                .city(request.getCity())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .genderPreference(request.getGenderPreference())
                .monthlyRent(request.getMonthlyRent())
                .securityDeposit(request.getSecurityDeposit())
                .foodIncluded(request.getFoodIncluded())
                .amenities(request.getAmenities())
                .houseRules(request.getHouseRules())
                .photos(request.getPhotos())
                .isActive(true)
                .totalBeds(0)
                .vacantBeds(0)
                .build();

        return listingRepository.save(listing);
    }

    /**
     * T-09: Get all active listings with pagination
     */
    public Page<Listing> getAllActiveListings(Pageable pageable) {
        return listingRepository.findByIsActiveTrue(pageable);
    }

    /**
     * T-10: Get a single listing by ID
     */
    public Listing getListingById(UUID id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id: " + id));
    }

    /**
     * T-11: Search/filter listings
     */
    public Page<Listing> searchListings(String area, Integer maxBudget,
                                         Listing.GenderPreference gender, Pageable pageable) {
        return listingRepository.searchListings(area, maxBudget, gender, pageable);
    }

    /**
     * Recalculates total_beds and vacant_beds for a listing.
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
