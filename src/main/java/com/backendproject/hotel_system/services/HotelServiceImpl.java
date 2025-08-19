package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Dtos.Requests.CreateHotelRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.CreateLocationRequestDto;
import com.backendproject.hotel_system.Dtos.Responses.HotelResponseDTO;
import com.backendproject.hotel_system.Models.HotelStars;
import com.backendproject.hotel_system.Exceptions.HotelNotFoundException;
import com.backendproject.hotel_system.Exceptions.UnAuthorizesAccess;
import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.Location;
import com.backendproject.hotel_system.repositories.HotelRepository;
import com.backendproject.hotel_system.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Hotel save(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel findHotelById(Long id) {
        Optional<Hotel> hotelOp = hotelRepository.findHotelById(id);
        if (hotelOp.isEmpty()) {
            throw new HotelNotFoundException("Hotel not found");
        }
        return hotelOp.get();
    }

    @Override
    public List<Hotel> findHotelByCity(String city) {
        List<Hotel> hotels = hotelRepository.findHotelByLocation_City(city);
        if (hotels == null || hotels.isEmpty()) {
            throw new HotelNotFoundException("Hotel not found");
        }
        return hotels;
    }

    @Override
    public List<Hotel> findHotelByStars(String stars) {
        List<Hotel> hotels = hotelRepository.findHotelByStars(stars);
        if (hotels.isEmpty()) {
            throw new HotelNotFoundException("Hotel not found");
        }
        return hotels;
    }

    @Override
    public List<Hotel> findHotelByCityAndStars(String city, String stars) {
        List<Hotel> hotels = hotelRepository.findHotelByLocation_CityAndStars(city, stars);
        if (hotels.isEmpty()) {
            throw new HotelNotFoundException("Hotel not found");
        }
        return hotels;
    }

    @Override
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Override
    public HotelResponseDTO addHotel(CreateHotelRequestDto hotelRequestDto) {
        try {
            CreateLocationRequestDto locationRequestDto = hotelRequestDto.getLocation();
            Location location = new Location();
            location.setAddress(locationRequestDto.getAddress());
            location.setCity(locationRequestDto.getCity());
            location.setCountry(locationRequestDto.getCountry());
            location.setState(locationRequestDto.getState());
            location.setZip(locationRequestDto.getZip());
            location.setLatitude(locationRequestDto.getLatitude());
            location.setLongitude(locationRequestDto.getLongitude());
            locationRepository.save(location);

            Hotel hotel = new Hotel();
            hotel.setName(hotelRequestDto.getName());
            hotel.setStars(HotelStars.valueOf(hotelRequestDto.getStars()));
            hotel.setLocation(location);
            hotelRepository.save(hotel);

            return HotelResponseDTO.from(hotel);
        } catch (UnAuthorizesAccess e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add hotel: " + e.getMessage(), e);
        }
    }
}
