package com.backendproject.hotel_system.services;

import com.backendproject.hotel_system.Models.Room;
import com.backendproject.hotel_system.Models.RoomType;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface RoomService {

    public List<Room> getAllRooms();
    public Room getRoomById(long id);
    public Room addRoom(String roomNumber, double price, String description, RoomType type,int capacity);
    public Room updateRoom(Room room);
    public Room deleteRoom(long id);
}
