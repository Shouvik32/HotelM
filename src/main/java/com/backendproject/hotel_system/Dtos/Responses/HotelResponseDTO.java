package com.backendproject.hotel_system.Dtos.Responses;


import com.backendproject.hotel_system.Models.Hotel;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class HotelResponseDTO {
    private Long id;
    private String name;
    private String stars;
    //private String ratingName;
    private Long locationId;
    private String locationAddress;
    private String city;
    private String country;
    private String state;
    private String zip;
    private double latitude;
    private double longitude;


    public static HotelResponseDTO from(Hotel hotel) {
        HotelResponseDTO dto = new HotelResponseDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setStars(hotel.getStars() != null ? hotel.getStars().toString() : null);
        if (hotel.getLocation() != null) {
            dto.setLocationId(hotel.getLocation().getId());
            dto.setLocationAddress(hotel.getLocation().getAddress());
            dto.setCity(hotel.getLocation().getCity());
            dto.setCountry(hotel.getLocation().getCountry());
            dto.setState(hotel.getLocation().getState());
            dto.setZip(hotel.getLocation().getZip());
            dto.setLatitude(hotel.getLocation().getLatitude());
            dto.setLongitude(hotel.getLocation().getLongitude());
         
        }
        return dto;
    }
    public static List<HotelResponseDTO> from(List<Hotel> hotels) {
        return hotels.stream()
                .map(HotelResponseDTO::from)
                .collect(Collectors.toList());
    }
}