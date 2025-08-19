package com.backendproject.hotel_system.Dtos.Requests;

import lombok.Data;

@Data
public class CreateHotelRequestDto {
    private String name;
    private String stars;
    private CreateLocationRequestDto location;
}
