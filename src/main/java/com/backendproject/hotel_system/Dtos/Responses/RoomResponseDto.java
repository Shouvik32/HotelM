package com.backendproject.hotel_system.Dtos.Responses;


import com.backendproject.hotel_system.Models.BaseModel;
import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Models.RoomType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.Setter;

import java.util.Date;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomResponseDto {

    private Long id;
    private String roomNumber;
    private double price;
    private RoomType roomType;
    private String description;
    private int capacity;

    public static RoomResponseDto from(Room room) {
        if (room == null) return null;
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setPrice(room.getPrice());
        dto.setCapacity(room.getCapacity());
        dto.setDescription(room.getDescription());
        return dto;
    }
}
