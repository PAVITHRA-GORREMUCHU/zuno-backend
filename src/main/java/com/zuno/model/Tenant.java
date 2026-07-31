package com.zuno.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    private String aadhaarFrontUrl;
    private String aadhaarBackUrl;

    @Column(nullable = false)
    private LocalDate moveInDate;

    private LocalDate moveOutDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @Column(updatable = false)
    private Instant createdAt;

    public enum TenantStatus {
        ACTIVE, MOVED_OUT
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        if (this.status == null) this.status = TenantStatus.ACTIVE;
    }
}
