package com.backendproject.hotel_system.repositories;

import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.HotelStars;
import com.backendproject.hotel_system.Models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    public Hotel save(Hotel hotel);
    public Optional<Hotel> findHotelById(Long id);
    public List<Hotel> findHotelByStars(String stars);
    public List<Hotel> findAll();
    public List<Hotel> findHotelByLocation_City(String city);
    public List<Hotel> findHotelByLocation_CityAndStars(String city, String stars);

}

