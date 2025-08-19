package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Dtos.Requests.RoomRequestDto;
import com.backendproject.hotel_system.Dtos.Requests.SuggestRoomRequest;
import com.backendproject.hotel_system.Models.Hotel;
import com.backendproject.hotel_system.Models.Room;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface RoomService {

    public List<Room> getAllRoomsByHotel(long hotelId);
    public Room getRoomById(long id);
    public List<Room> getRoomByType(long hotelId,String  roomType);
    public Room save(String roomNumber, double price, String description, String type, int capacity, Hotel hotel, boolean isAvailable);
    public Room updateRoom(Room room);
    public Room deleteRoom(long id);
    public List<Room> suggestedRooms(SuggestRoomRequest roomRequestDto, List<Room> availableRooms);
}
