package com.zuno.dto;

import com.zuno.model.Room;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotBlank(message = "Room label is required")
    private String roomLabel;

    @NotNull(message = "Room type is required")
    private Room.RoomType roomType;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 6, message = "Capacity cannot exceed 6")
    private Integer capacity;
}
