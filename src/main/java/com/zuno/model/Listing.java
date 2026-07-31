package com.zuno.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Listing {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String pgName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String city;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderPreference genderPreference;

    @Column(nullable = false)
    private Integer monthlyRent;

    private Integer securityDeposit;

    @Column(nullable = false)
    private Boolean foodIncluded;

    @ElementCollection
    @CollectionTable(name = "listing_amenities", joinColumns = @JoinColumn(name = "listing_id"))
    @Column(name = "amenity")
    private List<String> amenities;

    @ElementCollection
    @CollectionTable(name = "listing_house_rules", joinColumns = @JoinColumn(name = "listing_id"))
    @Column(name = "rule")
    private List<String> houseRules;

    @ElementCollection
    @CollectionTable(name = "listing_photos", joinColumns = @JoinColumn(name = "listing_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @Column(nullable = false)
    private Boolean isActive;

    private Integer totalBeds;
    private Integer vacantBeds;

    @Column(updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    public enum GenderPreference {
        MALE, FEMALE, ANY
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (this.isActive == null) this.isActive = true;
        if (this.totalBeds == null) this.totalBeds = 0;
        if (this.vacantBeds == null) this.vacantBeds = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
