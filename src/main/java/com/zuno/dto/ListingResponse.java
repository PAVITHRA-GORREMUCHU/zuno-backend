package com.zuno.dto;

import com.zuno.model.Listing;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ListingResponse {

    private UUID id;
    private String pgName;
    private String address;
    private String area;
    private String city;
    private Double latitude;
    private Double longitude;
    private String genderPreference;
    private Integer monthlyRent;
    private Integer securityDeposit;
    private Boolean foodIncluded;
    private List<String> amenities;
    private List<String> houseRules;
    private List<String> photos;
    private Boolean isActive;
    private Integer totalBeds;
    private Integer vacantBeds;
    private Instant createdAt;
    private Instant updatedAt;

    // Owner info
    private String ownerName;
    private String ownerPhone;

    /**
     * Convert entity to response DTO
     */
    public static ListingResponse from(Listing listing) {
        return ListingResponse.builder()
                .id(listing.getId())
                .pgName(listing.getPgName())
                .address(listing.getAddress())
                .area(listing.getArea())
                .city(listing.getCity())
                .latitude(listing.getLatitude())
                .longitude(listing.getLongitude())
                .genderPreference(listing.getGenderPreference().name())
                .monthlyRent(listing.getMonthlyRent())
                .securityDeposit(listing.getSecurityDeposit())
                .foodIncluded(listing.getFoodIncluded())
                .amenities(listing.getAmenities())
                .houseRules(listing.getHouseRules())
                .photos(listing.getPhotos())
                .isActive(listing.getIsActive())
                .totalBeds(listing.getTotalBeds())
                .vacantBeds(listing.getVacantBeds())
                .createdAt(listing.getCreatedAt())
                .updatedAt(listing.getUpdatedAt())
                .ownerName(listing.getOwner() != null ? listing.getOwner().getFullName() : null)
                .ownerPhone(listing.getOwner() != null ? listing.getOwner().getPhone() : null)
                .build();
    }
}
