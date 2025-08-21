package com.backendproject.hotel_system.Controllers;

//import com.backendproject.hotel_system.Dtos.*;
import com.backendproject.hotel_system.Dtos.Requests.CreateRoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.RoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SearchRoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SuggestRoomRequest;
import com.backendproject.hotel_system.Dtos.Responses.ApiResponse;
import com.backendproject.hotel_system.Dtos.Responses.RoomResponseDto;
import com.backendproject.hotel_system.Exceptions.RoomNotFoundException;
import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Strategies.RoomSuggestionStrategies.MaxGuestCapacitySuggestion;
import com.backendproject.hotel_system.Strategies.RoomSuggestionStrategies.RoomSuggestionStrategy;
import com.backendproject.hotel_system.services.HotelService;
import com.backendproject.hotel_system.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    MaxGuestCapacitySuggestion maxGuestCapacitySuggestion;
    @PostMapping
    @PreAuthorize("hasAuthority('HOTEL_ADD')")
    public ResponseEntity<ApiResponse<RoomResponseDto>> addRoom(@RequestBody CreateRoomRequestDto roomRequestDto) {
        try {
            boolean isAvailable = true;
            Hotel hotel = hotelService.findHotelById(roomRequestDto.getHotelId());
            Room room = roomService.save(
                    roomRequestDto.getRoomNumber(),
                    roomRequestDto.getPrice(),
                    roomRequestDto.getDescription(),
                    roomRequestDto.getRoomType().toUpperCase() ,
                    roomRequestDto.getCapacity(),
                    hotel,
                    isAvailable
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", "Room created successfully", RoomResponseDto.from(room)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("failure", "Room creation failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('HOTEL_VIEW')")
    public ResponseEntity<ApiResponse<RoomResponseDto>> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(new ApiResponse<>("success", "Room fetched successfully", RoomResponseDto.from(room)));
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("failure", "Room not found: " + e.getMessage(), null));
        }
    }
    @PreAuthorize("hasAuthority('HOTEL_VIEW')")
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getAllRooms(@PathVariable long hotelId) {
        try {
            List<Room> rooms = roomService.getAllRoomsByHotel(hotelId);
                    List<RoomResponseDto> response=rooms.stream()
                    .map(RoomResponseDto::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>("success", "Rooms fetched successfully", response));
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("failure", "No rooms found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("failure", "Something went wrong", null));
        }
    }
    @PreAuthorize("hasAuthority('HOTEL_VIEW')")
    @GetMapping("hotel/{hotelId}/type/{roomType}")
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getRoomsByType(@PathVariable long hotelId,@PathVariable String roomType) {
        try {
            List<Room> filteredRooms = roomService.getRoomByType(hotelId,roomType.toUpperCase());
            List<RoomResponseDto> responseDtos = filteredRooms.stream()
                    .map(RoomResponseDto::from)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>("success", "Rooms by type fetched successfully", responseDtos));
        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("failure", "No rooms of specified type found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("failure", "Error fetching rooms by type", null));
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('HOTEL_UPDATE')")
    public ResponseEntity<ApiResponse<RoomResponseDto>> updateRoom(@PathVariable Long id, @RequestBody RoomRequestDto requestDto) {
        try {
            Room updatedRoom = roomService.getRoomById(id);

            if (requestDto.getRoomNumber() != null) updatedRoom.setRoomNumber(requestDto.getRoomNumber());
            if (requestDto.getRoomType() != null) updatedRoom.setRoomType(requestDto.getRoomType());
            if (requestDto.getPrice() != null) updatedRoom.setPrice(requestDto.getPrice());
            if (requestDto.getCapacity() > 0) updatedRoom.setCapacity(requestDto.getCapacity());
            if (requestDto.getDescription() != null) updatedRoom.setDescription(requestDto.getDescription());

            Room room = roomService.updateRoom(updatedRoom);
            return ResponseEntity.ok(new ApiResponse<>("success", "Room updated successfully", RoomResponseDto.from(room)));
        }
        catch(RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("failure", "No rooms found", null));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("failure", "Room update failed: " + e.getMessage(), null));
        }
    }
    @PreAuthorize("hasAuthority('HOTEL_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok(new ApiResponse<>("success", "Room deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("failure", "Room deletion failed: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasAuthority('HOTEL_VIEW')")
    @PostMapping(value = "/hotel/{hotelId}/suggest")
    public ResponseEntity<List<RoomResponseDto>> suggestRooms(
            @PathVariable Long hotelId,
            @RequestBody SuggestRoomRequest requestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("AUTH in controller: " + auth);
        if (auth != null) {
            System.out.println("Principal: " + auth.getPrincipal());
            System.out.println("Authorities: " + auth.getAuthorities());
        }

        System.out.println("=== Room Suggestion Endpoint Called ===");
        System.out.println("Hotel ID: " + hotelId);
        System.out.println("Number of Guests: " + requestDto.getNoOfGuests());
        System.out.println("Check-in: " + requestDto.getCheckin());
        System.out.println("Check-out: " + requestDto.getCheckout());

        try {
            if (requestDto.getNoOfGuests() <= 0) {
                System.err.println("Invalid number of guests: " + requestDto.getNoOfGuests());
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }

            if (requestDto.getCheckin() == null || requestDto.getCheckout() == null) {
                System.err.println("Check-in or check-out date is null");
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }
            if (requestDto.getCheckin().after(requestDto.getCheckout())) {
                System.err.println("Check-in date is after check-out date");
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }

            System.out.println("Step 1: Fetching all rooms...");
            List<Room> allRooms = roomService.getAllRoomsByHotel(hotelId);
            System.out.println("Total rooms fetched: " + allRooms.size() + " rooms");

            if (allRooms.isEmpty()) {
                System.out.println("No rooms available");
                return ResponseEntity.ok(new ArrayList<>());
            }
            System.out.println("Step 2: Applying room suggestion strategy...");
            List<Room> suggestedRooms = maxGuestCapacitySuggestion.suggestRooms(requestDto, allRooms);
            System.out.println("Strategy completed. Suggested rooms: " + suggestedRooms.size());
            System.out.println("Step 3: Converting rooms to DTOs...");
            List<RoomResponseDto> response = suggestedRooms.stream()
                    .map(room -> {
                        try {
                            System.out.println("Converting room ID: " + room.getId() +
                                    ", Room Number: " + room.getRoomNumber() +
                                    ", Capacity: " + room.getCapacity());
                            return RoomResponseDto.from(room);
                        } catch (Exception e) {
                            System.err.println("Error converting room " + room.getId() + " to DTO: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());

            System.out.println("Successfully converted " + response.size() + " rooms to DTOs");
            System.out.println("=== Room Suggestion Completed Successfully ===");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("=== ERROR in Room Suggestion ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== END ERROR ===");

            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }
}