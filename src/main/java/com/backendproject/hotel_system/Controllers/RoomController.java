package com.backendproject.hotel_system.Controllers;


import com.backendproject.hotel_system.Dtos.CreateRoomRequestDto;
import com.backendproject.hotel_system.Dtos.ResponseStatus;
import com.backendproject.hotel_system.Dtos.SearchRoomRequestDto;
import com.backendproject.hotel_system.Dtos.RoomResponseDto;
import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.bookingStrategies.MaxGuestCapacitySuggestion;
import com.backendproject.hotel_system.bookingStrategies.RoomSuggestionStrategy;
import com.backendproject.hotel_system.repositories.RoomRepository;
import com.backendproject.hotel_system.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    @PostMapping(value = "/rooms")
    public RoomResponseDto addRooms(@RequestBody CreateRoomRequestDto roomRequestDto) {
        RoomResponseDto responseDto=new RoomResponseDto();;
      try{
          Room room = roomService.addRoom(roomRequestDto.getRoomNumber(),roomRequestDto.getPrice(),roomRequestDto.getDescription(),roomRequestDto.getRoomType(),roomRequestDto.getCapacity());
          responseDto.setId(room.getId());
          responseDto.setCreatedAt(room.getCreatedAt());
          responseDto.setUpdatedAt(room.getUpdatedAt());
          responseDto.setRoomNumber(room.getRoomNumber());
          responseDto.setPrice(room.getPrice());
          responseDto.setDescription(room.getDescription());
          responseDto.setCapacity(room.getCapacity());
          responseDto.setRoomType(room.getRoomType());
          responseDto.setCapacity(room.getCapacity());
          responseDto.setResponseStatus(ResponseStatus.SUCCESS);


      }
      catch (Exception e){

        responseDto.setResponseStatus(ResponseStatus.FAILURE);
      }

      return  responseDto;
    }
    @GetMapping(value = "/rooms/{id}")
    public RoomResponseDto getRoomById(SearchRoomRequestDto searchRoomRequestDto) {
        RoomResponseDto responseDto = new RoomResponseDto();

        try {
            long id = searchRoomRequestDto.getId();
            Room room = roomService.getRoomById(id);

            responseDto.setId(room.getId());
            responseDto.setCreatedAt(room.getCreatedAt());
            responseDto.setUpdatedAt(room.getUpdatedAt());
            responseDto.setRoomNumber(room.getRoomNumber());
            responseDto.setPrice(room.getPrice());
            responseDto.setDescription(room.getDescription());
            responseDto.setCapacity(room.getCapacity());
            responseDto.setRoomType(room.getRoomType());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            System.err.println("Error retrieving room by ID: " + e.getMessage());  // Logging the error
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }

        return responseDto;
    }

    @GetMapping(value = "/rooms")
    public List<RoomResponseDto>  getRooms(){

            List<RoomResponseDto> responseList = new ArrayList<>();

            try {
                List<Room> rooms = roomService.getAllRooms();

                for (Room room : rooms) {
                    RoomResponseDto responseDto = new RoomResponseDto();
                    responseDto.setId(room.getId());
                    responseDto.setCreatedAt(room.getCreatedAt());
                    responseDto.setUpdatedAt(room.getUpdatedAt());
                    responseDto.setRoomNumber(room.getRoomNumber());
                    responseDto.setPrice(room.getPrice());
                    responseDto.setDescription(room.getDescription());
                    responseDto.setCapacity(room.getCapacity());
                    responseDto.setRoomType(room.getRoomType());
                    responseDto.setResponseStatus(ResponseStatus.SUCCESS);

                    responseList.add(responseDto);
                }
            } catch (Exception e) {
                System.err.println("Error retrieving all rooms: " + e.getMessage());
            }

            return responseList;

    }
    @PatchMapping(value = "/rooms/{id}")
    public RoomResponseDto updateRoom(@PathVariable Long id,@RequestBody SearchRoomRequestDto requestDto) {
        RoomResponseDto responseDto=new RoomResponseDto();
        try {
            Room updatedRoom = roomService.getRoomById(id);
            if (requestDto.getRoomNumber() != null) {
                updatedRoom.setRoomNumber(requestDto.getRoomNumber());
            }
            if (requestDto.getRoomType() != null) {
                updatedRoom.setRoomType(requestDto.getRoomType());
            }
            if (requestDto.getPrice() != null) {  // Fix: Checking null instead of 0.0
                updatedRoom.setPrice(requestDto.getPrice());
            }
            if (requestDto.getCapacity() != 0) {
                updatedRoom.setCapacity(requestDto.getCapacity());
            }
            if (requestDto.getDescription() != null) {
                updatedRoom.setDescription(requestDto.getDescription());
            }
            Room room = roomService.updateRoom(updatedRoom);
            responseDto.setId(room.getId());
            responseDto.setRoomNumber(updatedRoom.getRoomNumber());
            responseDto.setRoomType(updatedRoom.getRoomType());
            responseDto.setPrice(updatedRoom.getPrice());
            responseDto.setCapacity(updatedRoom.getCapacity());
            responseDto.setDescription(updatedRoom.getDescription());
            responseDto.setCreatedAt(updatedRoom.getCreatedAt());
            responseDto.setUpdatedAt(updatedRoom.getUpdatedAt());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);

        }
        catch (Exception e){
            responseDto.setDescription(e.getMessage());
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return  responseDto;
    }
    @DeleteMapping(value = "/rooms/{id}")
    public RoomResponseDto deleteRoom(@PathVariable Long id) {
        RoomResponseDto responseDto=new RoomResponseDto();
        try{
            roomService.deleteRoom(id);
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        }
        catch (Exception e){
            responseDto.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    @GetMapping(value = "/rooms/hotel/{hotelId}/suggest")
    public List<RoomResponseDto> suggestRooms(@PathVariable Long hotelId, @RequestParam(defaultValue = "1") int noOfGuests) {
        List<RoomResponseDto> responseList = new ArrayList<>();
        
        try {
            System.out.println("Starting room suggestion for hotelId: " + hotelId + ", guests: " + noOfGuests);
            
            // Get all available rooms (since Room model doesn't have hotel relationship yet)
            List<Room> availableRooms = roomService.getAllRooms();
            System.out.println("Available rooms fetched: " + availableRooms.size() + " rooms");
            
            // Use suggestion strategy to get suggested rooms
            RoomSuggestionStrategy suggestionStrategy = new MaxGuestCapacitySuggestion();
            List<Room> suggestedRooms = suggestionStrategy.suggestRooms(noOfGuests, availableRooms);
            System.out.println("Suggested rooms: " + suggestedRooms.size() + " rooms");
            
            // Map suggested rooms to DTOs
            for (Room room : suggestedRooms) {
                RoomResponseDto responseDto = new RoomResponseDto();
                responseDto.setId(room.getId());
                responseDto.setCreatedAt(room.getCreatedAt());
                responseDto.setUpdatedAt(room.getUpdatedAt());
                responseDto.setRoomNumber(room.getRoomNumber());
                responseDto.setPrice(room.getPrice());
                responseDto.setDescription(room.getDescription());
                responseDto.setCapacity(room.getCapacity());
                responseDto.setRoomType(room.getRoomType());
                responseDto.setResponseStatus(ResponseStatus.SUCCESS);
                
                responseList.add(responseDto);
            }
            
            System.out.println("test c"); // This matches the log mentioned in problem statement
            System.out.println("Successfully mapped " + responseList.size() + " rooms to response DTOs");
            
        } catch (Exception e) {
            System.err.println("Error suggesting rooms: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
            
            // Return empty list with failure status in case of error
            RoomResponseDto errorResponse = new RoomResponseDto();
            errorResponse.setResponseStatus(ResponseStatus.FAILURE);
            errorResponse.setDescription("Error suggesting rooms: " + e.getMessage());
            responseList.add(errorResponse);
        }
        
        return responseList;
    }
}
