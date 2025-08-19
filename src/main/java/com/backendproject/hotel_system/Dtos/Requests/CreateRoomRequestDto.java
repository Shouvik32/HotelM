package com.backendproject.hotel_system.Dtos.Requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateRoomRequestDto {
    private Long hotelId;
    private String roomNumber;
    private double price;
    private String roomType;
    private String description;
    private int capacity;
    private boolean isAvailable;
}
