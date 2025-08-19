package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Dtos.Requests.CreateHotelRequestDto;
import com.backendproject.hotel_system.Dtos.Responses.HotelResponseDTO;
import com.backendproject.hotel_system.Models.Hotel;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface HotelService {
    public Hotel save(Hotel hotel);
    public HotelResponseDTO addHotel(CreateHotelRequestDto hotelRequestDto);
    public Hotel findHotelById(Long id);
    public List<Hotel> findAll();
    public List<Hotel> findHotelByCity(String city);
    public List<Hotel> findHotelByStars(String stars);
    public List<Hotel> findHotelByCityAndStars(String city, String stars);
}
