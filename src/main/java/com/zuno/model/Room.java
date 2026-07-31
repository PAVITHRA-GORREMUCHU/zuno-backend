package com.zuno.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @Column(nullable = false)
    private String roomLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer occupiedCount;

    @Column(updatable = false)
    private Instant createdAt;

    public enum RoomType {
        SINGLE, DOUBLE, TRIPLE
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        if (this.occupiedCount == null) this.occupiedCount = 0;
    }
}
