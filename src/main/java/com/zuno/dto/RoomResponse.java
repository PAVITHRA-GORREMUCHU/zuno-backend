package com.zuno.dto;

import com.zuno.model.Room;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class RoomResponse {

    private UUID id;
    private UUID listingId;
    private String roomLabel;
    private String roomType;
    private Integer capacity;
    private Integer occupiedCount;
    private Integer availableBeds;
    private Instant createdAt;

    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .listingId(room.getListing().getId())
                .roomLabel(room.getRoomLabel())
                .roomType(room.getRoomType().name())
                .capacity(room.getCapacity())
                .occupiedCount(room.getOccupiedCount())
                .availableBeds(room.getCapacity() - room.getOccupiedCount())
                .createdAt(room.getCreatedAt())
                .build();
    }
}
