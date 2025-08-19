package com.backendproject.hotel_system.Controllers;


import com.backendproject.hotel_system.Dtos.Requests.CreateHotelRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.CreateLocationRequestDto;
import com.backendproject.hotel_system.Dtos.Responses.ApiResponse;
import com.backendproject.hotel_system.Dtos.Responses.HotelResponseDTO;
import com.backendproject.hotel_system.Dtos.Responses.ResponseStatus;
import com.backendproject.hotel_system.Exceptions.HotelNotFoundException;
import com.backendproject.hotel_system.Exceptions.UnAuthorizesAccess;
import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.HotelStars;
import com.backendproject.hotel_system.Models.Location;
import com.backendproject.hotel_system.repositories.LocationRepository;
import com.backendproject.hotel_system.services.HotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;
@Slf4j
@RestController
@RequestMapping("/hotels")
public class HotelController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private LocationRepository locationRepository;
    @PreAuthorize("hasAuthority('HOTEL_VIEW')")
    @GetMapping
    public ResponseEntity<com.backendproject.hotel_system.Dtos.Responses.ApiResponse<List<HotelResponseDTO>>> getAllHotels() {
        try {
            //log.info("getAllHotels requested ");
            List<Hotel> hotels=hotelService.findAll();
            List<HotelResponseDTO> data=HotelResponseDTO.from(hotels);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>(ResponseStatus.SUCCESS.toString(),"Hotels fetched successfully",data));
        }
        catch (HotelNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(ResponseStatus.FAILURE.toString(),"Hotel not found",null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(ResponseStatus.FAILURE.toString(),e.getMessage(),null));
        }
    }
    @PostMapping("/")
    @PreAuthorize("hasAuthority('HOTEL_ADD')")
    public ResponseEntity<ApiResponse<HotelResponseDTO>> addHotels(@RequestBody CreateHotelRequestDto hotelRequestDto){
        try{

            HotelResponseDTO res=hotelService.addHotel(hotelRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("sucess","New Hotel added succesfully at"+res.getCity()+","+res.getCountry(),res));
        }
        catch (UnAuthorizesAccess e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(ResponseStatus.FAILURE.toString(),e.getMessage(),null));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("Failed",e.getMessage(),null));
        }
    }
    @PreAuthorize("hasAuthority('HOTEL_VIEW')")
    @GetMapping("/city/{city}")
  public ResponseEntity<ApiResponse<List<HotelResponseDTO>>>  getHotelsByCity(@PathVariable("city") String city){
        try {
            List<Hotel> hotels=hotelService.findHotelByCity(city);
           // HotelResponseDTO hotelResponseDto=new HotelResponseDTO();
            List<HotelResponseDTO> data=HotelResponseDTO.from(hotels);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>("Success","Hotel found",data));
        }
        catch (HotelNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("Failed","Hotel not found",null));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("Failed","Something went wrong",null));
        }

    }
}
